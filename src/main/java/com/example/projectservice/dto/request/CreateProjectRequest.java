package com.example.projectservice.dto.request;

import java.util.List;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.ProjectMember;
import com.example.projectservice.entity.Role;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
	@NotBlank
	String title,

	@NotBlank
	String content,

	@NotBlank
	Position myPosition,

	@NotBlank
	List<Position> needPositions
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
			.build();

		project.addMember(member);
		return project;
	}
}
