package com.example.projectservice.controller;

import java.util.List;

import com.example.projectservice.dto.request.UpdateProjectRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservice.dto.ApiResponse;
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.request.Filter;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Position;
import com.example.projectservice.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/project")
	public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
		@RequestBody CreateProjectRequest request,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.createProject(request, memberId)));
	}

	@GetMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<ProjectResponse>> getProject(
		@PathVariable("projectId") Long projectId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.findProjectById(projectId)));
	}

	@GetMapping("/projects")
	public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByFilter(
		@RequestParam("filter") Filter filter,
		@RequestParam(value = "position", required = false) Position position) {

		return ResponseEntity.ok(ApiResponse.success(projectService.findProjectsByFilter(filter, position)));
	}

	@GetMapping("/projects/my")
	public ResponseEntity<ApiResponse<List<ProjectResponse>>> getMyProjects(
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.findMyProjects(memberId)));
	}

	@PatchMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId,
		@RequestBody UpdateProjectRequest request) {

		return ResponseEntity.ok(ApiResponse.success(projectService.updateProject(projectId, memberId, request)));
	}

	@PatchMapping("/project/{projectId}/complete")
	public ResponseEntity<ApiResponse<ProjectResponse>> completeProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.completeProject(projectId, memberId)));
	}

	@DeleteMapping("/project/{projectId}")
	public ResponseEntity<ApiResponse<ProjectResponse>> deleteProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(projectService.deleteProject(projectId, memberId)));
	}
}
