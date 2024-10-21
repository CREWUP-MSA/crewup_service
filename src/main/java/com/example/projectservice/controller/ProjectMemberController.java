package com.example.projectservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservice.dto.ApiResponse;
import com.example.projectservice.dto.request.AddMemberToProjectRequest;
import com.example.projectservice.dto.request.UpdateMemberToProject;
import com.example.projectservice.dto.response.ProjectMemberResponse;
import com.example.projectservice.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api/{projectId}")
@RequiredArgsConstructor
public class ProjectMemberController {

	private final ProjectMemberService projectMemberService;

	@PostMapping("/member")
	public ResponseEntity<ApiResponse<ProjectMemberResponse>> createMemberToProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@RequestBody AddMemberToProjectRequest request) {

		return ResponseEntity.ok(ApiResponse.success(projectMemberService.createMemberToProject(projectId, requesterId, request)));
	}

	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<ProjectMemberResponse>>> getMembersOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId) {

		return ResponseEntity.ok(ApiResponse.success(projectMemberService.getMembersOfProject(projectId, requesterId)));
	}

	@PatchMapping("/member/{memberId}")
	public ResponseEntity<ApiResponse<ProjectMemberResponse>> updateMemberInProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId,
		@RequestBody UpdateMemberToProject request) {

		return ResponseEntity.ok(ApiResponse.success(projectMemberService.updateMemberInProject(projectId, requesterId, memberId, request)));
	}

	@PatchMapping("/member/{memberId}/leader")
	public ResponseEntity<ApiResponse<ProjectMemberResponse>> updateLeaderOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectMemberService.updateLeaderOfProject(projectId, requesterId, memberId)));
	}

	@DeleteMapping("/member/{memberId}")
	public ResponseEntity<ApiResponse<ProjectMemberResponse>> deleteMemberOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectMemberService.deleteMemberOfProject(projectId, requesterId, memberId)));
	}
}
