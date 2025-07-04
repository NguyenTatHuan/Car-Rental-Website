package com.springprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.springprojects.client")
public class BookingsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingsServiceApplication.class, args);
	}

}