package com.example.projectservice.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.projectservice.dto.CustomApiResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "401", description = "이미 완료된 프로젝트입니다.",
	content = @Content(
		schema = @Schema(implementation = CustomApiResponse.class),
		examples = {
			@ExampleObject(
				name = "이미 완료된 프로젝트입니다.",
				value = """
						{
							"message": "이미 완료된 프로젝트입니다.",
						}
						"""
			)
		}
	)
)
public @interface AlreadyCompleteApiResponse {
}
