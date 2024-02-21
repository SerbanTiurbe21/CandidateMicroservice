package com.example.candidate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "Candidate API", version = "1.0", description = "Questions API v1.0"))
public class CandidateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandidateApplication.class, args);
    }

}
