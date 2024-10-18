package com.example.projectservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectservice.dto.client.MemberResponse;
import com.example.projectservice.dto.request.CreateProjectRequest;
import com.example.projectservice.dto.response.ProjectResponse;
import com.example.projectservice.entity.Project;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
import com.example.projectservice.repository.ProjectRepository;
import com.example.projectservice.service.client.MemberServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final MemberServiceClient memberServiceClient;

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
		MemberResponse memberResponse = getMemberById(memberId);

		Project project = projectRepository.save(request.toEntity(memberResponse));

		log.info("Project created by member: {}", memberResponse.id());

		return ProjectResponse.from(project, memberId);
	}


	private MemberResponse getMemberById(Long memberId) {
		try{
			return memberServiceClient.getMemberById(memberId).data();
		} catch (FeignException.NotFound e){
			log.error("FROM Member-Service : Member not found");
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		} catch (FeignException e){
			log.error("FROM Member-Service : Member service error");
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
