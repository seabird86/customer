package com.anhnt.customer.config;

import com.anhnt.customer.config.intercepter.AppInterceptor;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInterceptor());
    }

    @Autowired OpenAPIService openAPIService;

    @Bean
    public OpenApiCustomiser customOpenAPI() {
        SpringDocUtils.getConfig()
            .replaceWithSchema(OffsetTime.class, new StringSchema().format("time").example(OffsetTime.now().format(DateTimeFormatter.ISO_OFFSET_TIME)))//"01:00:00+07:30"))
            .replaceWithSchema(LocalDate.class, new StringSchema().format("date").example(DateTimeFormatter.ISO_DATE.format(LocalDate.now())))
            .replaceWithSchema(Instant.class, new StringSchema().format("date-time").examples(List.of(DateTimeFormatter.ISO_INSTANT.format(Instant.now()),"2018-01-01T00:00:00+07:00")));
        return openApi -> {
        };
    }

    @Bean
    public OperationCustomizer customize(){
        return (operation,method) ->{
            ApiResponses responses = operation.getResponses();
            if (method.getMethod().getReturnType().equals(ResponseEntity.class)){
                Type type = ((ParameterizedType)((ParameterizedType)method.getMethod().getGenericReturnType()).getActualTypeArguments()[0]).getActualTypeArguments()[0];
                ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(type));
                Map<String, Schema> schemas = openAPIService.getCalculatedOpenAPI().getComponents().getSchemas();
                schemas.put(resolvedSchema.schema.getName(),resolvedSchema.schema);
                Schema schema = new ObjectSchema()
                        .type("object")
                        .addProperty("data",schemas.get(resolvedSchema.schema.getName()))
                        .name("BodyEntity<%s>".formatted(resolvedSchema.schema.getName()));
                schemas.put("BodyEntity<%s>".formatted(resolvedSchema.schema.getName()),schema);
                responses.addApiResponse("Success",new ApiResponse().content(new Content().addMediaType("application/json",new MediaType().schema(new ObjectSchema().$ref(schema.getName())))));
            }
            return operation;
        };
    }

}