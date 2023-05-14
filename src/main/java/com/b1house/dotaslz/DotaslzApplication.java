package com.b1house.dotaslz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DotaslzApplication {

	public static void main(String[] args) {
		SpringApplication.run(DotaslzApplication.class, args);
	}

}
