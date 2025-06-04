package com.kayky.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;

    @Bean(name = "brasilApiClient")
    public RestClient.Builder brasilApiClient() {
        return RestClient.builder().baseUrl(brasilApiConfigurationProperties.baseUrl());
    }


}
