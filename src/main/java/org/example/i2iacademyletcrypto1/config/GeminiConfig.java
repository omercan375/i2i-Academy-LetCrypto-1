package org.example.i2iacademyletcrypto1.config;

import com.google.genai.Client;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GeminiProperties.class)
public class GeminiConfig {

    @Bean
    public Client geminiApiClient(GeminiProperties properties) {

        return Client.builder()
                .apiKey(properties.apiKey())
                .build();
    }
}