package com.example.projectservice.dto.request;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.entity.project.Position;
import com.example.projectservice.entity.project.Project;
import com.example.projectservice.entity.project.ProjectRecruit;

public record RecruitProjectRequest(
	Position position
) {
	public ProjectRecruit toEntity(Project project, MemberResponse memberResponse) {
		return ProjectRecruit.builder()
				.memberId(memberResponse.id())
				.project(project)
				.position(position)
				.build();
	}
}
