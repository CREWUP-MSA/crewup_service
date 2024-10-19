package com.example.projectservice.dto.response;

import com.example.projectservice.entity.Position;

public record ProjectMemberResponse(
	Long id,
	String nickname,
	Position position,
	boolean isDeleted
) {
}
