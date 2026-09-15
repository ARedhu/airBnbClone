package com.Ashish.airBnbClone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnbCloneApplication.class, args);
		System.out.println("Server is running...");
	}

}
