package com.example.projectservice.dto.request;

import com.example.projectservice.entity.project.Position;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberToProject(
	@NotBlank(message = "변경할 포지션을 입력해주세요.")
	Position position
) {

}
