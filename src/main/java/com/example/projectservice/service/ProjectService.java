package com.example.projectservice.service;

import java.util.List;

import com.example.projectservice.dto.client.mapper.MemberClientMapper;
import com.example.projectservice.dto.request.UpdateProjectRequest;
import com.example.projectservice.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.request.Filter;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final MemberClientMapper memberClientMapper;

	/**
	 * 프로젝트 생성
	 * @param request 프로젝트 생성 요청 정보
	 * @param memberId 생성 요청한 멤버 ID
	 * @return ProjectResponse
	 * @throws CustomException 멤버를 찾을 수 없는 경우
	 * @see ErrorCode
	 */
	@Transactional
	public ProjectResponse createProject(CreateProjectRequest request, Long memberId) {
		MemberResponse memberResponse = memberClientMapper.getMemberById(memberId);

		Project project = projectRepository.save(request.toEntity(memberResponse));

		log.info("Project created by member: {}", memberResponse.id());

		return ProjectResponse.from(project);
	}

	/**
	 * 프로젝트 상세 조회
	 * @param id 조회할 프로젝트 ID
	 * @return ProjectResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 */
	public ProjectResponse findProjectById(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		return ProjectResponse.from(project);
	}

	/**
	 * 프로젝트 목록 조회
	 * @param filter 필터
	 *               - ALL: 전체 프로젝트 조회
	 *               - RECRUITING: 모집 중인 프로젝트 조회
	 *               - COMPLETED: 완료된 프로젝트 조회
	 *               - NEED_POSITION: 필요한 포지션에 따라 프로젝트 조회
	 * @param position 포지션 (필터가 NEED_POSITION 인 경우 필수)
	 * @return List<ProjectResponse>
	 */
	public List<ProjectResponse> findProjectsByFilter(Filter filter, Position position) {
		if (filter.equals(Filter.NEED_POSITION))
			if (position == null)
				throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

		List<Project> projects = projectRepository.findProjectsByFilter(filter, position);
		return projects.stream()
			.map(ProjectResponse::from)
			.toList();
	}

	/**
	 * 내 프로젝트 목록 조회
	 * @param memberId 조회 요청한 멤버 ID
	 * @return List<ProjectResponse>
	 */
	public List<ProjectResponse> findMyProjects(Long memberId) {
		List<Project> projects = projectRepository.findMyProjects(memberId);

		return projects.stream()
			.map(ProjectResponse::from)
			.toList();
	}

	/**
	 * 프로젝트 수정 (리더만 수정 가능)
	 * @param projectId 수정할 프로젝트 ID
	 * @param memberId 수정 요청한 멤버 ID
	 * @param request 수정할 프로젝트 정보
	 * @return ProjectResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 리더가 아닌 경우
	 */
	@Transactional
	public ProjectResponse updateProject(Long projectId, Long memberId, UpdateProjectRequest request) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		validateLeader(project, memberId);

		project.update(request);

		return ProjectResponse.from(project);
	}

	/**
	 * 프로젝트 완료 (리더만 완료 가능)
	 * @param projectId 완료할 프로젝트 ID
	 * @param memberId 완료 요청한 멤버 ID
	 * @return ProjectResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 리더가 아닌 경우
	 * @throws CustomException (ALREADY_COMPLETED_PROJECT) 이미 완료된 프로젝트인 경우
	 */
	@Transactional
	public ProjectResponse completeProject(Long projectId, Long memberId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		validateLeader(project, memberId);

		project.complete();
		return ProjectResponse.from(project);
	}

	/**
	 * 프로젝트 삭제 (프로젝트 삭제 시 프로젝트 멤버도 함께 삭제, 리더만 삭제 가능)
	 * @param projectId 삭제할 프로젝트 ID
	 * @param memberId 삭제 요청한 멤버 ID
	 * @return ProjectResponse
	 * @throws CustomException (PROJECT_NOT_FOUND) 프로젝트를 찾을 수 없는 경우
	 * @throws CustomException (FORBIDDEN) 리더가 아닌 경우
	 */
	@Transactional
	public ProjectResponse deleteProject(Long projectId, Long memberId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		validateLeader(project, memberId);

		projectRepository.delete(project);
		return ProjectResponse.from(project);
	}

	private void validateLeader(Project project, Long memberId) {
		 if (!project.isLeader(memberId))
			 throw new CustomException(ErrorCode.FORBIDDEN);
	}
}
