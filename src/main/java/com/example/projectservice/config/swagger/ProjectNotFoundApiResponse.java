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
@ApiResponse(responseCode = "404", description = "프로젝트가 존재하지 않음",
	content = @Content(
		schema = @Schema(implementation = CustomApiResponse.class),
		examples = {
			@ExampleObject(
				name = "프로젝트가 존재하지 않음",
				value = """
						{
							"message": "해당 프로젝트를 찾을 수 없습니다.",
						}
						"""
			)
		}
	)
)
public @interface ProjectNotFoundApiResponse {
}
