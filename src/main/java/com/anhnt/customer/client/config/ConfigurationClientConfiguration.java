package com.anhnt.customer.client.config;

import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class ConfigurationClientConfiguration {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("ANHNT", "ANHNT-PASS");
    }

}
