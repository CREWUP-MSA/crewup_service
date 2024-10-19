package com.example.projectservice.repository.querydsl.impl;

import java.util.List;

import com.example.projectservice.dto.request.Filter;
import com.example.projectservice.entity.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Repository;

import com.example.projectservice.repository.querydsl.CustomProjectRepository;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomProjectRepositoryImpl implements CustomProjectRepository {

	private final JPQLQueryFactory queryFactory;

	/**
	 * 특정 멤버가 참여한 프로젝트 목록 조회
	 * @param memberId 멤버 ID
	 * @return List<Project>
	 */
	public List<Project> findMyProjects(Long memberId) {
		QProject project = QProject.project;
		QProjectMember projectMember = QProjectMember.projectMember;

		return queryFactory.selectFrom(project)
				.innerJoin(project.members, projectMember)
				.where(projectMember.memberId.eq(memberId))
				.orderBy(project.createdAt.desc())
				.fetch();
	}

	/**
	 * 프로젝트 필터링 조회
	 * @param filter 필터
	 * @param position 포지션
	 * @return List<Project>
	 */
	public List<Project> findProjectsByFilter(Filter filter, Position position) {
		QProject project = QProject.project;
		BooleanBuilder builder = new BooleanBuilder();

		switch (filter) {
			case ALL:
				break;
			case RECRUITING:
				builder.and(project.status.eq(Status.RECRUITING));
				break;
			case COMPLETED:
				builder.and(project.status.eq(Status.COMPLETED));
				break;
			case NEED_POSITION:
				builder.and(project.needPositions.contains(position));
		}

		return queryFactory.selectFrom(project)
				.where(builder)
				.orderBy(project.createdAt.desc())
				.fetch();
	}

}
