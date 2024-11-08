package com.example.projectservice.controller;

import com.example.projectservice.config.swagger.*;
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

import com.example.projectservice.dto.CustomApiResponse;
import com.example.projectservice.dto.request.RecruitProjectRequest;
import com.example.projectservice.dto.response.ProjectRecruitResponse;
import com.example.projectservice.service.ProjectRecruitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/crewup-service/api")
@RequiredArgsConstructor
@Tag(name = "프로젝트 모집 지원 API", description = "프로젝트 모집 지원 관련 API")
public class ProjectRecruitController {

	private final ProjectRecruitService projectRecruitService;

	@PostMapping("/{projectId}/recruit")
	@Operation(summary = "프로젝트 모집 지원", description = "프로젝트에 모집에 지원합니다.")
	@ApiResponse(responseCode = "200", description = "프로젝트 모집 지원 성공")
	@AlreadyCompleteApiResponse
	@AlreadyMemberApiResponse
	@AlreadyRecruitApiResponse
	public ResponseEntity<CustomApiResponse<ProjectRecruitResponse>> recruitToProject(
		@PathVariable Long projectId,
		@RequestHeader("X-Member-Id") Long requesterId,
		@RequestBody RecruitProjectRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.addRecruitToProject(projectId, requesterId, request)));
	}

	@GetMapping("/recruits/my")
	@Operation(summary = "내가 신청한 프로젝트 모집 조회", description = "내가 지원한 프로젝트 모집을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "내가 신청한 프로젝트 모집 조회 성공")
	@ProfileNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<List<ProjectRecruitResponse>>> getMyRecruits(
		@RequestHeader("X-Member-Id") Long requesterId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.getMyRecruits(requesterId)));
	}

	@GetMapping("/{projectId}/recruits")
	@Operation(summary = "프로젝트에 신청한 멤버 조회", description = "프로젝트에 지원한 멤버를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "프로젝트에 신청한 멤버 조회 성공")
	@ProjectNotFoundApiResponse
	@ProfileNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<List<ProjectRecruitResponse>>> getRecruitsToProject(
		@PathVariable Long projectId,
		@RequestHeader("X-Member-Id") Long requesterId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.getRecruitsToProject(projectId, requesterId)));
	}

	@PatchMapping("/{projectId}/recruit/{recruitId}/accept")
	@Operation(summary = "프로젝트 지원자 수락", description = "프로젝트 지원자를 수락합니다.")
	@ApiResponse(responseCode = "200", description = "프로젝트 지원자 수락 성공")
	@ProjectNotFoundApiResponse
	@ProjectRecruitNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<Boolean>> acceptRecruitToProject(
		@PathVariable Long projectId,
		@PathVariable Long recruitId,
		@RequestHeader("X-Member-Id") Long requesterId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.acceptRecruitToProject(projectId, recruitId, requesterId)));
	}

	@PatchMapping("/{projectId}/recruit/{recruitId}/reject")
	@Operation(summary = "프로젝트 지원자 거절", description = "프로젝트 지원자를 거절합니다.")
	@ApiResponse(responseCode = "200", description = "프로젝트 지원자 거절 성공")
	@ProjectNotFoundApiResponse
	@ProjectRecruitNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<Boolean>> rejectRecruitToProject(
		@PathVariable Long projectId,
		@PathVariable Long recruitId,
		@RequestHeader("X-Member-Id") Long requesterId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.rejectRecruitToProject(projectId, recruitId, requesterId)));
	}

	@DeleteMapping("/{projectId}/recruit/{recruitId}")
	@Operation(summary = "프로젝트 지원 취소", description = "프로젝트 지원 신청을 취소합니다.")
	@ApiResponse(responseCode = "200", description = "프로젝트 지원 취소 성공")
	@ProjectNotFoundApiResponse
	@ProjectRecruitNotFoundApiResponse
	@AlreadyResolvedRecruitApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<Boolean>> deleteRecruitToProject(
		@PathVariable Long projectId,
		@PathVariable Long recruitId,
		@RequestHeader("X-Member-Id") Long requesterId) {

		return ResponseEntity.ok(CustomApiResponse.success(projectRecruitService.deleteRecruitToProject(projectId, recruitId, requesterId)));
	}
}
