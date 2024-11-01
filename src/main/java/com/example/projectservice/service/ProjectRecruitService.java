package com.example.projectservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.dto.client.mapper.MemberClientMapper;
import com.example.projectservice.dto.request.RecruitProjectRequest;
import com.example.projectservice.dto.response.ProjectRecruitResponse;
import com.example.projectservice.entity.profile.Profile;
import com.example.projectservice.entity.project.Project;
import com.example.projectservice.entity.project.ProjectRecruit;
import com.example.projectservice.entity.project.Status;
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
	private final ProfileRepository profileRepository;
	private final MemberClientMapper memberClientMapper;

	/**
	 * 프로젝트에 지원자 추가
	 * @param projectId 프로젝트 ID
	 * @param memberId 지원자 ID
	 * @param request 프로젝트 지원 요청 정보
	 * @return ProjectRecruitResponse
	 */
	@Transactional
	public ProjectRecruitResponse addRecruitToProject(Long projectId, Long memberId, RecruitProjectRequest request) {
		MemberResponse memberResponse = memberClientMapper.getMemberById(memberId);
		Project project = findProjectById(projectId);

		if (project.getStatus().equals(Status.COMPLETED))
			throw new CustomException(ErrorCode.ALREADY_COMPLETED_PROJECT);

		ProjectRecruit projectRecruit = request.toEntity(project, memberResponse);
		project.addRecruit(projectRecruit);

		projectRecruitRepository.save(projectRecruit);
		Profile profile = findProfileByMemberId(memberId);

		return ProjectRecruitResponse.from(projectRecruit, profile.getNickname());
	}

	/**
	 * 내 지원 목록 조회
	 * @param memberId 멤버 ID
	 * @return List<ProjectRecruitResponse>
	 */
	public List<ProjectRecruitResponse> getMyRecruits(Long memberId) {
		List<ProjectRecruit> projectRecruits = projectRecruitRepository.findByMemberId(memberId);
		Profile profile = findProfileByMemberId(memberId);

		return projectRecruits.stream()
			.map(projectRecruit -> ProjectRecruitResponse.from(projectRecruit, profile.getNickname()))
			.toList();
	}

	/**
	 * 프로젝트에 지원한 지원자 목록 조회
	 * @param projectId 프로젝트 ID
	 * @return List<ProjectRecruitResponse>
	 */
	public List<ProjectRecruitResponse> getRecruitsToProject(Long projectId) {
		List<ProjectRecruit> projectRecruits = projectRecruitRepository.findByProjectId(projectId);

		return projectRecruits.stream()
			.map(projectRecruit -> {
				Profile profile = findProfileByMemberId(projectRecruit.getMemberId());
				return ProjectRecruitResponse.from(projectRecruit, profile.getNickname());
			})
			.toList();
	}

	public Object acceptRecruitToProject(Long projectId, Long recruitId, Long memberId) {
		return null;
	}

	public Object rejectRecruitToProject(Long projectId, Long recruitId, Long memberId) {
		return null;
	}

	public Object deleteRecruitToProject(Long projectId, Long recruitId, Long memberId) {
		return null;
	}

	private Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}

	private Profile findProfileByMemberId(Long memberId) {
		return profileRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));
	}
}
