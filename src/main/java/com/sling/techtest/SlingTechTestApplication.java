package com.sling.techtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SlingTechTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlingTechTestApplication.class, args);
    }

}
