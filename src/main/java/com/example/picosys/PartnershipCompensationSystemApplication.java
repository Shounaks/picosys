package com.example.picosys;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(
		name = "jwt_token_security",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER)
public class PartnershipCompensationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartnershipCompensationSystemApplication.class, args);
	}

}
