package org.example.i2iacademyletcrypto1.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;


    @Service
    @RequiredArgsConstructor
    public class SessionService {

        private static final String SESSION_KEY_PREFIX = "session:";
        private static final Duration SESSION_TTL = Duration.ofDays(3650);

        private final StringRedisTemplate sessionRedisTemplate;

        // Token üret ve Redis'e yaz
        public String createSession(UUID userId) {
            String token = UUID.randomUUID().toString();


            /*redis part*/
            sessionRedisTemplate.opsForValue().set(
                    SESSION_KEY_PREFIX + token,
                    userId.toString(),
                    SESSION_TTL
            );

            return token;
        }

        // Token'dan userId'yi çek (sonraki isteklerde doğrulama için)
        public UUID getUserIdByToken(String token) {
            String userId = sessionRedisTemplate.opsForValue().get(SESSION_KEY_PREFIX + token);
            return userId == null ? null : UUID.fromString(userId);
        }

        // Logout için
        public void deleteSession(String token) {
            sessionRedisTemplate.delete(SESSION_KEY_PREFIX + token);
        }
    }


