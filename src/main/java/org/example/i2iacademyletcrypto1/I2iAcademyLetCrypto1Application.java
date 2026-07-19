package org.example.i2iacademyletcrypto1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class I2iAcademyLetCrypto1Application {

    public static void main(String[] args) {
        SpringApplication.run(I2iAcademyLetCrypto1Application.class, args);
    }

}
