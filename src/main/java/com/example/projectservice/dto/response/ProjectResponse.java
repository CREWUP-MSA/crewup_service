package com.example.projectservice.dto.response;

import com.example.projectservice.entity.project.Category;
import com.example.projectservice.entity.project.Position;
import com.example.projectservice.entity.project.Project;
import com.example.projectservice.entity.project.Status;

import lombok.Builder;

import java.util.Set;

@Builder
public record ProjectResponse(
	Long id,
	String title,
	String content,
	Set<Position> needPositions,
	Set<Category> categories,
	Status status
) {
	public static ProjectResponse from(Project project) {
		return ProjectResponse.builder()
			.id(project.getId())
			.title(project.getTitle())
			.content(project.getContent())
			.needPositions(project.getNeedPositions())
			.categories(project.getCategories())
			.status(project.getStatus())
			.build();
	}
}
