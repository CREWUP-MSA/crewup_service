package com.example.projectservice.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.kafka.annotation.KafkaListener;
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
	private final RedissonClient redissonClient;

	/**
	 * 프로필 생성 - Kafka 이벤트 수신하여 회원가입시 자동 생성
	 * @param memberId 생성할 프로필의 회원 ID
	 *
	 * nickname: "USER" + memberId ( 기본 닉네임 )
	 */
	@KafkaListener(topics = "member-create", groupId = "crewup-service-profile-group", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void CreateProfile(Long memberId){
		Profile profile = Profile.builder()
			.memberId(memberId)
			.nickname("USER" + memberId)
			.introduction("자기소개를 작성해주세요.")
			.build();

		profileRepository.save(profile);
		log.info("profile created: {}", profile);
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
	@KafkaListener(topics = "member-delete", groupId = "crewup-service-profile-group", containerFactory = "kafkaListenerContainerFactory")
	@Transactional
	public void deleteProfile(Long memberId) {
		String lockKey = "member-delete-lock:" + memberId;
		RLock lock = redissonClient.getLock(lockKey);

		try {
			if (lock.tryLock(10, 5, TimeUnit.SECONDS)) {
				Profile profile = findProfile(memberId);
				profileRepository.delete(profile);
				log.info("profile deleted: {}", profile);
			} else {
				log.error("Failed to acquire lock: {}", lockKey);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Interrupted while trying to acquire lock for memberId: {}", memberId, e);
		} finally {
			if (lock.isHeldByCurrentThread())
				lock.unlock();
		}
	}

	private Profile findProfile(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
	}
}
