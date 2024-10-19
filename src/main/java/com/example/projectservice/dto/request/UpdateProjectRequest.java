package com.example.projectservice.dto.request;

import com.example.projectservice.entity.Position;

import java.util.List;

public record UpdateProjectRequest(
    String title,
    String content,
    List<Position> needPositions
) {
}
