package com.anhnt.customer.config;

import com.anhnt.common.domain.response.ErrorEntity;
import com.anhnt.common.domain.response.ErrorFactory.CustomerError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import feign.FeignException;

import com.anhnt.common.domain.exception.AbstractException;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

// https://www.baeldung.com/rest-api-error-handling-best-practices
// https://google.github.io/styleguide/jsoncstyleguide.xml
// http://docs.oasis-open.org/odata/odata-json-format/v4.0/errata02/os/odata-json-format-v4.0-errata02-os-complete.html#_Toc403940655
@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return CustomerError.INTERNAL_SERVER_ERROR.apply(List.of(e.getMessage())).toResponseEntity();
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity handleException(AbstractException e) {
        return e.getError().toResponseEntity();
    }

    @ExceptionHandler(FeignException.class)
    @ResponseBody
    public ResponseEntity handleException(FeignException e) throws JsonMappingException, JsonProcessingException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
    	headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(e.contentUTF8(),headers,HttpStatus.valueOf(e.status()));
    }

}
