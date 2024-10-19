package com.example.projectservice.dto.request;

import java.util.Set;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.entity.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(
	@NotBlank(message = "프로젝트 제목을 입력해주세요.")
	String title,

	@NotBlank(message = "프로젝트 내용을 입력해주세요.")
	String content,

	@NotBlank(message = "프로젝트 내 포지션을 입력해주세요.")
	Position myPosition,

	@NotEmpty(message = "프로젝트에 필요한 포지션을 입력해주세요.")
	@Size(min = 1, message = "프로젝트에 필요한 포지션을 최소 1개 이상 입력해주세요.")
	Set<Position> needPositions
) {

	/**
	 * CreateProjectRequest 를 Project 엔티티로 변환
	 * @param memberResponse 멤버 응답
	 * @return Project
	 */
	public Project toEntity(MemberResponse memberResponse) {
		ProjectMember member = ProjectMember.builder()
			.role(Role.LEADER)
			.position(myPosition)
			.memberId(memberResponse.id())
			.build();

		Project project = Project.builder()
			.title(title)
			.content(content)
			.needPositions(needPositions)
			.status(Status.RECRUITING)
			.build();

		project.addMember(member);
		return project;
	}
}
