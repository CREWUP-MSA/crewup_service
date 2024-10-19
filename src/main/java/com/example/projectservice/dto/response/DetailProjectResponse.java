package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Project;

import lombok.Builder;

@Builder
public record DetailProjectResponse(
	Long id,
	String title,
	String content,
	String myPosition,
	String leaderName
) {

	public static DetailProjectResponse from(Project project) {
		return DetailProjectResponse.builder()
			.id(project.getId())
			.title(project.getTitle())
			.content(project.getContent())
			.build();
	}
}
