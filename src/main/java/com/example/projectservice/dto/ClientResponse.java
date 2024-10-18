package com.example.projectservice.dto;

public record ClientResponse<T> (
	T data,
	String message
) {
}
