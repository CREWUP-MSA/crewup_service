package com.example.projectservice.service;

import com.example.projectservice.dto.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.response.ProjectResponse;
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
	private final MemberMapper memberMapper;

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
		MemberResponse memberResponse = memberMapper.getMemberById(memberId);

		Project project = projectRepository.save(request.toEntity(memberResponse));

		log.info("Project created by member: {}", memberResponse.id());

		return ProjectResponse.from(project, memberId);
	}

	public Object findProjectById(Long id) {
		return null;
	}

	public Object findProjects() {
		return null;
	}

	public Object findMyProjects(Long memberId) {
		return null;
	}
}
