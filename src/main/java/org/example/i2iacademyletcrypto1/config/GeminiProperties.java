package org.example.i2iacademyletcrypto1.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gemini")
public record GeminiProperties(
        String apiKey,
        String model
) {
}