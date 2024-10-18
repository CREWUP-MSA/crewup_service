package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Project;

import lombok.Builder;

@Builder
public record ProjectResponse(
	Long id,
	String title,
	String content,
	Long memberId
) {
	public static ProjectResponse from(Project project, Long memberId) {
		return ProjectResponse.builder()
			.id(project.getId())
			.title(project.getTitle())
			.content(project.getContent())
			.memberId(memberId)
			.build();
	}
}
