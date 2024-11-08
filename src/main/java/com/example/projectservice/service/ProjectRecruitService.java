package com.example.projectservice.service;

import java.util.List;

import com.example.projectservice.entity.project.*;
import com.example.projectservice.repository.ProjectMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.dto.client.mapper.MemberClientMapper;
import com.example.projectservice.dto.request.RecruitProjectRequest;
import com.example.projectservice.dto.response.ProjectRecruitResponse;
import com.example.projectservice.entity.profile.Profile;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.repository.ProfileRepository;
import com.example.projectservice.repository.ProjectRecruitRepository;
import com.example.projectservice.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectRecruitService {

	private final ProjectRecruitRepository projectRecruitRepository;
	private final ProjectRepository projectRepository;
	private final ProjectMemberRepository projectMemberRepository;
	private final ProfileRepository profileRepository;
	private final MemberClientMapper memberClientMapper;

	/**
	 * 프로젝트에 지원자 추가
	 * @param projectId 프로젝트 ID
	 * @param requesterId 지원자 ID
	 * @param request 프로젝트 지원 요청 정보
	 * @return ProjectRecruitResponse
	 */
	@Transactional
	public ProjectRecruitResponse addRecruitToProject(Long projectId, Long requesterId, RecruitProjectRequest request) {
		MemberResponse memberResponse = memberClientMapper.getMemberById(requesterId);
		Project project = findProjectById(projectId);

		validateAddRecruit(requesterId, project);

		ProjectRecruit projectRecruit = request.toEntity(project, memberResponse);
		project.addRecruit(projectRecruit);

		projectRecruitRepository.save(projectRecruit);
		Profile profile = findProfileByMemberId(requesterId);

		log.info("recruit added: {}", projectRecruit.getId());
		return ProjectRecruitResponse.from(projectRecruit, profile.getNickname());
	}



	/**
	 * 내 지원 목록 조회
	 * @param requesterId 멤버 ID
	 * @return List<ProjectRecruitResponse>
	 */
	public List<ProjectRecruitResponse> getMyRecruits(Long requesterId) {
		List<ProjectRecruit> projectRecruits = projectRecruitRepository.findByMemberId(requesterId);
		Profile profile = findProfileByMemberId(requesterId);

		return projectRecruits.stream()
			.map(projectRecruit -> ProjectRecruitResponse.from(projectRecruit, profile.getNickname()))
			.toList();
	}

	/**
	 * 프로젝트에 지원한 지원자 목록 조회 - 리더만 조회 가능
	 * @param projectId 프로젝트 ID
	 * @param requesterId 요청자 ID
	 * @return List<ProjectRecruitResponse>
	 */
	public List<ProjectRecruitResponse> getRecruitsToProject(Long projectId, Long requesterId) {
		Project project = findProjectById(projectId);
		validateLeader(requesterId, project);

		List<ProjectRecruit> projectRecruits = projectRecruitRepository.findByProjectId(projectId);

		return projectRecruits.stream()
			.map(projectRecruit -> {
				Profile profile = findProfileByMemberId(projectRecruit.getMemberId());
				return ProjectRecruitResponse.from(projectRecruit, profile.getNickname());
			})
			.toList();
	}

	/**
	 * 프로젝트 지원자 수락 - 리더만 수락 가능
	 * @param projectId 프로젝트 ID
	 * @param recruitId 지원자 ID
	 * @param requesterId 요청자 ID
	 * @return Boolean
	 */
	@Transactional
	public boolean acceptRecruitToProject(Long projectId, Long recruitId, Long requesterId) {
		Project project = findProjectById(projectId);
		validateLeader(requesterId, project);

		ProjectRecruit projectRecruit = findProjectRecruit(recruitId);
		projectRecruit.accept();

		ProjectMember projectMember = ProjectMember.from(projectRecruit);
		project.addMember(projectMember);
		projectMemberRepository.save(projectMember);

		log.info("recruit accepted: {}", projectRecruit.getId());
		return true;
	}

	/**
	 * 프로젝트 지원자 거절 - 리더만 거절 가능
	 * @param projectId 프로젝트 ID
	 * @param recruitId 지원자 ID
	 * @param requesterId 요청자 ID
	 * @return Boolean
	 */
	@Transactional
	public boolean rejectRecruitToProject(Long projectId, Long recruitId, Long requesterId) {
		Project project = findProjectById(projectId);
		validateLeader(requesterId, project);

		ProjectRecruit projectRecruit = findProjectRecruit(recruitId);
		projectRecruit.reject();

		log.info("recruit rejected: {}", projectRecruit.getId());
		return true;
	}

	/**
	 * 프로젝트 지원 취소 - 지원자 본인만 취소 가능
	 * @param projectId 프로젝트 ID
	 * @param recruitId 지원자 ID
	 * @param requesterId 요청자 ID
	 * @return Boolean
	 */
	@Transactional
	public boolean deleteRecruitToProject(Long projectId, Long recruitId, Long requesterId) {
		Project project = findProjectById(projectId);
		ProjectRecruit projectRecruit = findProjectRecruit(recruitId);

		validateSelfOrResolved(requesterId, projectRecruit);
		project.getRecruits().remove(projectRecruit);

		log.info("recruit deleted: {}", projectRecruit.getId());
		return true;
	}

	private Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}

	private Profile findProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
	}

	private ProjectRecruit findProjectRecruit(Long recruitId) {
		return projectRecruitRepository.findById(recruitId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_RECRUIT_NOT_FOUND));
	}

	private void validateLeader(Long requesterId, Project project) {
		if (!project.isLeader(requesterId))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}

	private void validateSelfOrResolved(Long requesterId, ProjectRecruit projectRecruit) {
		if (!projectRecruit.getMemberId().equals(requesterId))
			throw new CustomException(ErrorCode.FORBIDDEN);

		if (!projectRecruit.getStatus().equals(RecruitStatus.PENDING))
			throw new CustomException(ErrorCode.ALREADY_RESOLVED_RECRUIT);
	}

	private void validateAddRecruit(Long requesterId, Project project) {
		if (project.getStatus().equals(Status.COMPLETED))
			throw new CustomException(ErrorCode.ALREADY_COMPLETED_PROJECT);
		if (projectRecruitRepository.existsByProjectIdAndMemberId(project.getId(), requesterId))
			throw new CustomException(ErrorCode.ALREADY_RECRUITED);
		if (projectMemberRepository.existsByProjectIdAndMemberId(project.getId(), requesterId))
			throw new CustomException(ErrorCode.ALREADY_MEMBER);
	}
}
