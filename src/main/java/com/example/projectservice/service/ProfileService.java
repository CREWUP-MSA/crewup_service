package com.example.projectservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.request.UpdateProfileRequest;
import com.example.projectservice.dto.response.ProfileResponse;
import com.example.projectservice.entity.Profile;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProfileService {

	private final ProfileRepository profileRepository;

	/**
	 * 프로필 조회
	 * @param memberId 조회할 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException 프로필이 존재하지 않을 경우
	 */
	public ProfileResponse findProfileByMemberId(Long memberId) {
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

		return ProfileResponse.from(profile);
	}

	/**
	 * 내 프로필 조회
	 * @param memberId 내 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException 프로필이 존재하지 않을 경우
	 */
	public ProfileResponse findMyProfile(Long memberId) {
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

		return ProfileResponse.from(profile);
	}

	/**
	 * 프로필 수정
	 * @param request 수정할 프로필 정보
	 * @param memberId 수정할 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException 프로필이 존재하지 않을 경우
	 */
	@Transactional
	public ProfileResponse updateProfile(UpdateProfileRequest request, Long memberId) {
		Profile profile = profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

		profile.update(request);
		return ProfileResponse.from(profile);
	}
}
