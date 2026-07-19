package org.example.i2iacademyletcrypto1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.math.BigDecimal;


    @Configuration
    public class RedisConfig {

        // Key: String (örn. "BTC"), Value: BigDecimal (örn. 65000.50)
        @Bean(name = "assetPrice")
        public RedisTemplate<String, BigDecimal> assetPrice(RedisConnectionFactory connectionFactory) {
            RedisTemplate<String, BigDecimal> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new GenericToStringSerializer<>(BigDecimal.class));
            return template;
        }
    }

