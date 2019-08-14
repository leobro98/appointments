package com.leobro.appointment.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.leobro.appointment")
@EnableJpaRepositories("com.leobro.appointment.data")
@EntityScan(basePackages = {"com.leobro.appointment.data"})
public class AppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);
	}
}
