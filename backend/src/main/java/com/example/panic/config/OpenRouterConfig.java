// src/main/java/com/example/panic/config/OpenRouterConfig.java
package com.example.panic.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class OpenRouterConfig {

    @Value("${openrouter.api.base-url}")
    private String baseUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Bean
    public WebClient openRouterClient() {
        // Force use of the JDK DNS resolver (avoids search-domain NXDOMAIN)
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE);

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}