package com.gokceerer.Mentoree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class MentoreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentoreeApplication.class, args);
	}

}
