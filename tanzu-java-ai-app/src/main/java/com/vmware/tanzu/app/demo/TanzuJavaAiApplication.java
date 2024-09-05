package com.vmware.tanzu.app.demo;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

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

    // TODO: Do as I say, not as I do
    @Bean
    @Scope("prototype")
    WebClient.Builder webClientBuilder() throws SSLException {
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
