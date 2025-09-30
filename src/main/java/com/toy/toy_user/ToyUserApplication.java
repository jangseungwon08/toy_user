package com.toy.toy_user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToyUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToyUserApplication.class, args);
	}

}
