package com.example.projectservice.controller;

import java.util.List;

import com.example.projectservice.config.swagger.AlreadyCompleteApiResponse;
import com.example.projectservice.config.swagger.ForbiddenApiResponse;
import com.example.projectservice.config.swagger.MustInputKeywordApiResponse;
import com.example.projectservice.config.swagger.MustInputPositionApiResponse;
import com.example.projectservice.config.swagger.ProjectNotFoundApiResponse;
import com.example.projectservice.dto.request.CategoryFilter;
import com.example.projectservice.dto.request.UpdateProjectRequest;
import com.example.projectservice.entity.Category;
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

import com.example.projectservice.dto.CustomApiResponse;
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.request.Filter;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Position;
import com.example.projectservice.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api")
@RequiredArgsConstructor
@Tag(name = "프로젝트 API", description = "프로젝트 모집관련 API")
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping("/project")
	@Operation(summary = "프로젝트 모집 생성", description = "프로젝트 모집을 생성합니다.")
	@ProjectNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<ProjectResponse>> createProject(
		@RequestBody @Valid CreateProjectRequest request,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.createProject(request, memberId)));
	}

	@GetMapping("/project/{projectId}")
	@Operation(summary = "프로젝트 모집 조회", description = "프로젝트 모집을 조회합니다.")
	@ProjectNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<ProjectResponse>> getProject(
		@PathVariable("projectId") Long projectId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.findProjectById(projectId)));
	}

	@GetMapping("/projects")
	@Operation(summary = "프로젝트 모집 목록 조회", description = "프로젝트 모집 목록을 조회합니다.")
	@MustInputKeywordApiResponse
	@MustInputPositionApiResponse
	public ResponseEntity<CustomApiResponse<List<ProjectResponse>>> getProjectsByFilter(
		@RequestParam("filter") Filter filter,
		@RequestParam("category") CategoryFilter categoryFilter,
		@RequestParam(value = "position", required = false) Position position,
		@RequestParam(value = "keyword", required = false) String keyword) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.findProjectsByFilter(filter, position, categoryFilter, keyword)));
	}

	@GetMapping("/projects/my")
	@Operation(summary = "내 프로젝트 모집 목록 조회", description = "내 프로젝트 모집 목록을 조회합니다.")
	public ResponseEntity<CustomApiResponse<List<ProjectResponse>>> getMyProjects(
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.findMyProjects(memberId)));
	}

	@PatchMapping("/project/{projectId}")
	@Operation(summary = "프로젝트 모집 수정", description = "프로젝트 모집을 수정합니다.")
	@ProjectNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<ProjectResponse>> updateProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId,
		@RequestBody UpdateProjectRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.updateProject(projectId, memberId, request)));
	}

	@PatchMapping("/project/{projectId}/complete")
	@Operation(summary = "프로젝트 모집 완료", description = "프로젝트 모집을 완료합니다.")
	@ProjectNotFoundApiResponse
	@ForbiddenApiResponse
	@AlreadyCompleteApiResponse
	public ResponseEntity<CustomApiResponse<ProjectResponse>> completeProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.completeProject(projectId, memberId)));
	}

	@DeleteMapping("/project/{projectId}")
	@Operation(summary = "프로젝트 모집 삭제", description = "프로젝트 모집을 삭제합니다.")
	@ProjectNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<ProjectResponse>> deleteProject(
		@PathVariable("projectId") Long projectId,
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectService.deleteProject(projectId, memberId)));
	}
}
