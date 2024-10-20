package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.ProjectMember;
import com.example.projectservice.entity.Role;

import lombok.Builder;

@Builder
public record ProjectMemberResponse(
	Long id,
	Long memberId,
	String nickname,
	Position position,
	Long projectId,
	Role role
) {
	public static ProjectMemberResponse from(ProjectMember projectMember, String nickname) {
		return ProjectMemberResponse.builder()
			.id(projectMember.getId())
			.projectId(projectMember.getProject().getId())
			.nickname(nickname)
			.position(projectMember.getPosition())
			.projectId(projectMember.getProject().getId())
			.role(projectMember.getRole())
			.build();
	}
}
