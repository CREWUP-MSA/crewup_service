package com.example.projectservice.dto.request;

import com.example.projectservice.entity.project.Category;
import com.example.projectservice.entity.project.Position;

import java.util.Set;

public record UpdateProjectRequest(
    String title,
    String content,
    Set<Position> needPositions,
	Set<Category> categories
) {
}
