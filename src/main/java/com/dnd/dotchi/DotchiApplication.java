package com.dnd.dotchi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DotchiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotchiApplication.class, args);
	}

}
