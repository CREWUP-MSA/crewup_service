package com.example.projectservice.service;

import java.util.List;

import com.example.projectservice.dto.client.mapper.MemberClientMapper;
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
	 * @throws CustomException 프로젝트를 찾을 수 없는 경우
	 * @see ErrorCode
	 */
	public ProjectResponse findProjectById(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		return ProjectResponse.from(project);
	}

	// TODO : 프로젝트 목록 조회 (필터링)
	public Object findProjectsByFilter(Filter filter, Position position) {
		if (filter.equals(Filter.NEED_POSITION))
			if (position == null)
				throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);


		return null;
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

	// TODO : 프로젝트 수정
	public Object updateProject(Long projectId) {
		return null;
	}

	// TODO : 프로젝트 완료
	public Object completeProject(Long projectId) {
		return null;
	}

	// TODO : 프로젝트 삭제
	public Object deleteProject(Long projectId) {
		return null;
	}
}
