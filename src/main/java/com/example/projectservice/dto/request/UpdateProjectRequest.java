package com.example.projectservice.dto.request;

import com.example.projectservice.entity.Category;
import com.example.projectservice.entity.Position;

import java.util.Set;

public record UpdateProjectRequest(
    String title,
    String content,
    Set<Position> needPositions,
	Set<Category> categories
) {
}
