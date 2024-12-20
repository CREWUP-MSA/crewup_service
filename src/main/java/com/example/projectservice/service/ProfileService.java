package com.example.projectservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.aop.RedissonLock;
import com.example.projectservice.config.kafka.KafkaTopic;
import com.example.projectservice.dto.request.UpdateProfileRequest;
import com.example.projectservice.dto.response.ProfileResponse;
import com.example.projectservice.entity.profile.Profile;
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
	 * 프로필 생성 - Kafka 이벤트 수신하여 회원가입시 자동 생성
	 * @param memberId 생성할 프로필의 회원 ID
	 *
	 * nickname: "USER" + memberId ( 기본 닉네임 )
	 */
	@KafkaListener(topics = KafkaTopic.MEMBER_CREATED, groupId = "crewup-service-profile-group", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void createProfile(Long memberId){
		Profile profile = Profile.builder()
			.memberId(memberId)
			.nickname("USER" + memberId)
			.introduction("자기소개를 작성해주세요.")
			.build();

		profileRepository.save(profile);
		log.info("profile created: {}", memberId);
	}

	/**
	 * 프로필 조회
	 * @param memberId 조회할 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필이 존재하지 않을 경우
	 */
	public ProfileResponse findProfileByMemberId(Long memberId) {
		Profile profile = findProfile(memberId);

		return ProfileResponse.from(profile);
	}

	/**
	 * 내 프로필 조회
	 * @param memberId 내 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필이 존재하지 않을 경우
	 */
	public ProfileResponse findMyProfile(Long memberId) {
		Profile profile = findProfile(memberId);

		return ProfileResponse.from(profile);
	}

	/**
	 * 프로필 수정
	 * @param request 수정할 프로필 정보
	 * @param memberId 수정할 회원 ID
	 * @return ProfileResponse
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필이 존재하지 않을 경우
	 */
	@RedissonLock(lockKey = "profile-lock:#memberId")
	@Transactional
	public ProfileResponse updateProfile(UpdateProfileRequest request, Long memberId) {
		Profile profile = findProfile(memberId);

		profile.update(request);
		return ProfileResponse.from(profile);
	}

	/**
	 * 멤버 탈퇴 시 프로필 삭제
	 * Redisson Lock 을 사용하여 멤버 삭제 동기화 처리
	 * @param memberId 삭제할 멤버 ID
	 */
	@KafkaListener(topics = KafkaTopic.MEMBER_DELETE, groupId = "crewup-service-profile-group", containerFactory = "kafkaListenerContainerFactory")
	@RedissonLock(lockKey = "profile-lock:#memberId")
	@Transactional
	public void deleteProfile(Long memberId) {
		Profile profile = findProfile(memberId);
		profileRepository.delete(profile);
		log.info("profile deleted: {}", profile);
	}

	private Profile findProfile(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
	}
}
