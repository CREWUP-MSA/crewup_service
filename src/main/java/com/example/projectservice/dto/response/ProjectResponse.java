package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Category;
import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.Status;

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
