package com.example.volunteer_platform;
import java.lang.String;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class VolunteerPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolunteerPlatformApplication.class, args);
	}

}
