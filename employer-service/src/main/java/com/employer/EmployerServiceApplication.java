package com.employer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.employer.feign")
public class EmployerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployerServiceApplication.class, args);
	}

}
