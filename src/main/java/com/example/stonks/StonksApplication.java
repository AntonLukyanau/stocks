package com.example.stonks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StonksApplication {

    public static void main(String[] args) {
        SpringApplication.run(StonksApplication.class, args);
    }

}
