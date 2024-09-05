package com.vmware.tanzu.app.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class TanzuJavaAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TanzuJavaAiApplication.class, args);
    }

    // TODO: Remove once https://github.com/spring-projects/spring-ai/issues/563 resolved
    @Bean
    @Scope("prototype")
    RestClient.Builder restClientBuilder() {
        return RestClient.builder().requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS));
    }
}
