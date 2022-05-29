package com.anhnt.customer.client.config;

import com.anhnt.common.domain.response.ConfigurationCache;
import com.anhnt.customer.client.ConfigurationClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfiguration {
    @Bean
    public LocaleResolver localeResolver(ConfigurationClient client){
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setSupportedLocales(List.of(Locale.FRENCH, Locale.ITALIAN));
        ConfigurationCache.cache(client.getMessages().getBody().getData().getMessages());
        return localeResolver;
    }
}
