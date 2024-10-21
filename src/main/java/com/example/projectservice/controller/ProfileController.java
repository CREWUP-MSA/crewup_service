package com.example.projectservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectservice.dto.ApiResponse;
import com.example.projectservice.dto.request.UpdateProfileRequest;
import com.example.projectservice.dto.response.ProfileResponse;
import com.example.projectservice.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crewup-service/api")
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/profile/{memberId}")
	public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(
		@PathVariable("memberId") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(profileService.findProfileByMemberId(memberId)));
	}

	@GetMapping("/profile/my")
	public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile(
		@RequestHeader("X-Member-Id") Long memberId) {

		return ResponseEntity.ok(ApiResponse.success(profileService.findMyProfile(memberId)));
	}

	@PatchMapping("/profile")
	public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
		@RequestHeader("X-Member-Id") Long memberId,
		@RequestBody UpdateProfileRequest request) {

		return ResponseEntity.ok(ApiResponse.success(profileService.updateProfile(request, memberId)));
	}

}
