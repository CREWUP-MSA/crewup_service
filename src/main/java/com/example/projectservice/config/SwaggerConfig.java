package com.example.projectservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.addServersItem(new Server().url("/"))
			.info(info());
	}

	private Info info() {
		return new Info()
			.title("Crewup Service API")
			.version("1.0")
			.description("API Documentation for Crewup Service")
			.license(new License().name("Apache 2.0").url("http://springdoc.org"));
	}
}
