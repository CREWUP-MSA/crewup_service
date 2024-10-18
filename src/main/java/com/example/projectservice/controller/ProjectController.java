package com.example.projectservice.controller;

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
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/project-service/api")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/project")
	public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
		@RequestBody CreateProjectRequest request,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.createProject(request, memberId)));
	}

	@GetMapping("/project")
	public ResponseEntity<ApiResponse<?>> getProject() {
		// TODO : 프로젝트 상세 조회
		return null;
	}

	@GetMapping("/projects")
	public ResponseEntity<ApiResponse<?>> getProjects() {
		// TODO : 프로젝트 목록 조회
		return null;
	}

	@GetMapping("/projects/my")
	public ResponseEntity<ApiResponse<?>> getMyProjects() {
		// TODO : 내 프로젝트 목록 조회
		return null;
	}

	@PatchMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<?>> updateProject(
		@PathVariable("projectId") Long projectId) {
		// TODO : 프로젝트 수정
		return null;
	}

	@DeleteMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<?>> deleteProject(
		@PathVariable("projectId") Long projectId) {
		// TODO : 프로젝트 삭제
		return null;
	}
}
