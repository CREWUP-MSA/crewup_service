package com.example.projectservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservice.config.swagger.AlreadyLeaderApiResponse;
import com.example.projectservice.config.swagger.CannotDeleteLeaderApiResponse;
import com.example.projectservice.config.swagger.ForbiddenApiResponse;
import com.example.projectservice.config.swagger.MemberNotFoundApiResponse;
import com.example.projectservice.config.swagger.ProfileNotFoundApiResponse;
import com.example.projectservice.config.swagger.ProjectNotFoundApiResponse;
import com.example.projectservice.dto.CustomApiResponse;
import com.example.projectservice.dto.request.UpdateMemberToProject;
import com.example.projectservice.dto.response.ProjectMemberResponse;
import com.example.projectservice.service.ProjectMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api/{projectId}")
@RequiredArgsConstructor
@Tag(name = "프로젝트 멤버 API", description = "프로젝트 멤버 관련 API")
public class ProjectMemberController {

	private final ProjectMemberService projectMemberService;

	@GetMapping("/members")
	@Operation(summary = "프로젝트 멤버 목록 조회", description = "프로젝트의 멤버 목록을 조회합니다.")
	@ProfileNotFoundApiResponse
	@ProjectNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<List<ProjectMemberResponse>>> getMembersOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId) {

		return ResponseEntity.ok(
			CustomApiResponse.success(projectMemberService.getMembersOfProject(projectId, requesterId)));
	}

	@PatchMapping("/member/{memberId}")
	@Operation(summary = "프로젝트 멤버 수정", description = "프로젝트의 멤버 정보를 수정합니다.")
	@ProfileNotFoundApiResponse
	@ProjectNotFoundApiResponse
	@MemberNotFoundApiResponse
	@ForbiddenApiResponse
	public ResponseEntity<CustomApiResponse<ProjectMemberResponse>> updateMemberInProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId,
		@RequestBody @Valid UpdateMemberToProject request) {

		return ResponseEntity.ok(
			CustomApiResponse.success(projectMemberService.updateMemberInProject(projectId, requesterId, memberId, request)));
	}

	@PatchMapping("/member/{memberId}/leader")
	@Operation(summary = "프로젝트 리더 변경", description = "프로젝트의 리더를 변경합니다.")
	@ProjectNotFoundApiResponse
	@ProfileNotFoundApiResponse
	@MemberNotFoundApiResponse
	@ForbiddenApiResponse
	@AlreadyLeaderApiResponse
	public ResponseEntity<CustomApiResponse<ProjectMemberResponse>> updateLeaderOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId) {

		return ResponseEntity.ok(
			CustomApiResponse.success(projectMemberService.updateLeaderOfProject(projectId, requesterId, memberId)));
	}

	@DeleteMapping("/member/{memberId}")
	@Operation(summary = "프로젝트 멤버 삭제", description = "프로젝트의 멤버를 삭제합니다.")
	@ProjectNotFoundApiResponse
	@ProfileNotFoundApiResponse
	@MemberNotFoundApiResponse
	@ForbiddenApiResponse
	@CannotDeleteLeaderApiResponse
	public ResponseEntity<CustomApiResponse<ProjectMemberResponse>> deleteMemberOfProject(
		@RequestHeader("X-Member-Id") Long requesterId,
		@PathVariable Long projectId,
		@PathVariable Long memberId) {

		return ResponseEntity.ok(
			CustomApiResponse.success(projectMemberService.deleteMemberOfProject(projectId, requesterId, memberId)));
	}
}
