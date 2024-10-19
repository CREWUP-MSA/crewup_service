package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.Status;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectResponse(
	Long id,
	String title,
	String content,
	List<Position> needPositions,
	Status status
) {
	public static ProjectResponse from(Project project) {
		return ProjectResponse.builder()
			.id(project.getId())
			.title(project.getTitle())
			.content(project.getContent())
			.needPositions(project.getNeedPositions())
			.status(project.getStatus())
			.build();
	}
}
