package com.example.projectservice.service;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.aop.RedissonLock;
import com.example.projectservice.config.kafka.KafkaTopic;
import com.example.projectservice.dto.request.UpdateMemberToProject;
import com.example.projectservice.dto.response.ProjectMemberResponse;
import com.example.projectservice.entity.profile.Profile;
import com.example.projectservice.entity.project.Project;
import com.example.projectservice.entity.project.ProjectMember;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.repository.ProfileRepository;
import com.example.projectservice.repository.ProjectMemberRepository;
import com.example.projectservice.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectMemberService {

	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final ProfileRepository profileRepository;


	/**
	 * 프로젝트 멤버 목록 조회 (프로젝트 멤버만 조회 가능)
	 * @param projectId 조회할 프로젝트 ID
	 * @param requesterId 요청자 ID
	 * @return List<ProjectMemberResponse>
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 요청자가 프로젝트 멤버가 아닌 경우
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필을 찾을 수 없는 경우
	 */
	public List<ProjectMemberResponse> getMembersOfProject(Long projectId, Long requesterId) {
		Project project = findProjectById(projectId);

		if (!project.isMember(requesterId))
			throw new CustomException(ErrorCode.FORBIDDEN);

		return project.getMembers().stream()
			.map(projectMember -> {
				Profile profile = findProfileByMemberId(projectMember.getMemberId());
				return ProjectMemberResponse.from(projectMember, profile.getNickname());
			})
			.toList();
	}

	/**
	 * 프로젝트 멤버 수정 (프로젝트 리더, 본인만 수정 가능)
	 * @param projectId 수정할 프로젝트 ID
	 * @param requesterId 요청자 ID
	 * @param memberId 수정할 멤버 ID
	 * @param request 수정할 프로젝트 멤버 정보
	 * @return ProjectMemberResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 요청자가 프로젝트 리더, 본인이 아닌 경우
	 * @throws CustomException (MEMBER_NOT_FOUND) 프로젝트 멤버를 찾을 수 없는 경우
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필을 찾을 수 없는 경우
	 */
	@RedissonLock(lockKey = "project-member-lock:#projectId")
	@Transactional
	public ProjectMemberResponse updateMemberInProject(Long projectId, Long requesterId, Long memberId, UpdateMemberToProject request) {
		Project project = findProjectById(projectId);
		validateLeaderOrSelf(requesterId, memberId, project);

		ProjectMember projectMember = findProjectMember(projectId, memberId);

		projectMember.update(request);
		Profile profile = findProfileByMemberId(memberId);
		return ProjectMemberResponse.from(projectMember, profile.getNickname());
	}

	/**
	 * 프로젝트 리더 변경 (프로젝트 리더만 변경 가능)
	 * @param projectId 변경할 프로젝트 ID
	 * @param requesterId 요청자 ID
	 * @param memberId 변경할 멤버 ID
	 * @return ProjectMemberResponse - 리더로 변경한 멤버 정보
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 요청자가 프로젝트 리더가 아닌 경우
	 * @throws CustomException (ALREADY_LEADER) 이미 리더인 경우
	 * @throws CustomException (MEMBER_NOT_FOUND) 프로젝트 멤버를 찾을 수 없는 경우
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필을 찾을 수 없는 경우
	 */
	@RedissonLock(lockKey = "project-member-lock:#projectId")
	@Transactional
	public ProjectMemberResponse updateLeaderOfProject(Long projectId, Long requesterId, Long memberId) {
		Project project = findProjectById(projectId);
		validateLeader(requesterId, project);

		if (requesterId.equals(memberId))
			throw new CustomException(ErrorCode.ALREADY_LEADER);

		ProjectMember leader = findProjectMember(projectId, requesterId);
		ProjectMember member = findProjectMember(projectId, memberId);

		leader.updateLeader(member);
		Profile profile = findProfileByMemberId(memberId);
		return ProjectMemberResponse.from(member, profile.getNickname());
	}

	/**
	 * 프로젝트 멤버 삭제 (프로젝트 리더, 본인만 삭제 가능)
	 * @param projectId 삭제할 프로젝트 ID
	 * @param requesterId 요청자 ID
	 * @param memberId 삭제할 멤버 ID
	 * @return ProjectMemberResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 요청자가 프로젝트 리더, 본인이 아닌 경우
	 * @throws CustomException (CANNOT_DELETE_LEADER) 리더인 경우
	 * @throws CustomException (MEMBER_NOT_FOUND) 프로젝트 멤버를 찾을 수 없는 경우
	 * @throws CustomException (PROFILE_NOT_FOUND) 프로필을 찾을 수 없는 경우
	 */
	@RedissonLock(lockKey = "project-member-lock:#projectId")
	@Transactional
	public ProjectMemberResponse deleteMemberOfProject(Long projectId, Long requesterId, Long memberId) {
		Project project = findProjectById(projectId);
		validateLeaderOrSelf(requesterId, memberId, project);

		if (project.isLeader(memberId))
			throw new CustomException(ErrorCode.CANNOT_DELETE_LEADER);

		ProjectMember projectMember = findProjectMember(projectId, memberId);

		project.getMembers().remove(projectMember);

		Profile profile = findProfileByMemberId(memberId);
		return ProjectMemberResponse.from(projectMember, profile.getNickname());
	}

	/**
	 * 멤버 탈퇴시 프로젝트 멤버 삭제
	 * Redisson Lock 을 사용하여 멤버 삭제 동기화 처리
	 * @param memberId 삭제할 멤버 ID
	 */
	@KafkaListener(topics = KafkaTopic.MEMBER_DELETE, groupId = "crewup-service-project-member-group", containerFactory = "kafkaListenerContainerFactory")
	@RedissonLock(lockKey = "project-member-lock:#memberId")
	@Transactional
	public void deleteMemberOfProject(Long memberId) {
		projectMemberRepository.deleteByMemberId(memberId);
		log.info("project members deleted - reason: Member Deleted: {}", memberId);
	}

	private Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}

	private ProjectMember findProjectMember(Long projectId, Long memberId) {
		return projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private Profile findProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
	}

	private void validateLeader(Long requesterId, Project project) {
		if (!project.isLeader(requesterId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}

	private void validateLeaderOrSelf(Long requesterId, Long memberId, Project project) {
		if (!project.isLeader(requesterId) && !requesterId.equals(memberId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}
}
