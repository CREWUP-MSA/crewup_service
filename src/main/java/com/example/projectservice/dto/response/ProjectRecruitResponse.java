package com.example.projectservice.dto.response;

import com.example.projectservice.entity.project.ProjectRecruit;

import lombok.Builder;

@Builder
public record ProjectRecruitResponse(
	Long id,
	Long memberId,
	Long projectId,
	String projectTitle,
	String nickname,
	String position
) {

	public static ProjectRecruitResponse from(ProjectRecruit projectRecruit, String nickname) {
		return ProjectRecruitResponse.builder()
			.id(projectRecruit.getId())
			.memberId(projectRecruit.getMemberId())
			.projectId(projectRecruit.getProject().getId())
			.projectTitle(projectRecruit.getProject().getTitle())
			.nickname(nickname)
			.position(projectRecruit.getPosition().name())
			.build();
	}
}
