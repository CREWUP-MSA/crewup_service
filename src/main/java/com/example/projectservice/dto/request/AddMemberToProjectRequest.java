package com.example.projectservice.dto.request;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.ProjectMember;
import com.example.projectservice.entity.Role;

import jakarta.validation.constraints.NotBlank;

public record AddMemberToProjectRequest(
	@NotBlank(message = "프로젝트 내 포지션을 입력해주세요.")
	Position position
) {

	public ProjectMember toEntity(Project project, MemberResponse memberResponse) {
		return ProjectMember.builder()
			.memberId(memberResponse.id())
			.role(Role.MEMBER)
			.position(this.position)
			.project(project)
			.build();
	}
}