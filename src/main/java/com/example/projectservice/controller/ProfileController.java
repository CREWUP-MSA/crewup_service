package com.example.projectservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservice.config.swagger.ProfileNotFoundApiResponse;
import com.example.projectservice.dto.CustomApiResponse;
import com.example.projectservice.dto.request.UpdateProfileRequest;
import com.example.projectservice.dto.response.ProfileResponse;
import com.example.projectservice.service.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api")
@RequiredArgsConstructor
@Tag(name = "프로필 API", description = "프로필 관련 API")
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/profile/{memberId}")
	@Operation(summary = "프로필 조회", description = "회원의 프로필을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "프로필 조회 성공")
	@ProfileNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<ProfileResponse>> getProfile(
		@PathVariable("memberId") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(profileService.findProfileByMemberId(memberId)));
	}

	@GetMapping("/profile/my")
	@Operation(summary = "내 프로필 조회", description = "내 프로필을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "프로필 조회 성공")
	@ProfileNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<ProfileResponse>> getMyProfile(
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(CustomApiResponse.success(profileService.findMyProfile(memberId)));
	}

	@PatchMapping("/profile")
	@Operation(summary = "프로필 수정", description = "회원의 프로필을 수정합니다.")
	@ApiResponse(responseCode = "200", description = "프로필 수정 성공")
	@ProfileNotFoundApiResponse
	public ResponseEntity<CustomApiResponse<ProfileResponse>> updateProfile(
		@RequestHeader("X-Member-Id") Long memberId,
		@RequestBody UpdateProfileRequest request) {

		return ResponseEntity.ok(CustomApiResponse.success(profileService.updateProfile(request, memberId)));
	}

}
