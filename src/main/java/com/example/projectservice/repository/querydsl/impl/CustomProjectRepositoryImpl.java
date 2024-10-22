package com.example.projectservice.repository.querydsl.impl;

import java.util.List;

import com.example.projectservice.dto.request.CategoryFilter;
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
	public List<Project> findProjectsByFilter(Filter filter, Position position, CategoryFilter categoryFilter) {
		QProject project = QProject.project;
		BooleanBuilder builder = new BooleanBuilder();

		if (filter != Filter.ALL) {
			switch (filter) {
				case RECRUITING:
				case COMPLETED:
					builder.and(project.status.eq(Status.valueOf(filter.name())));
					break;
				case NEED_POSITION:
					builder.and(project.needPositions.contains(position));
			}
		}

		if (categoryFilter != CategoryFilter.ALL)
			builder.and(project.categories.contains(Category.valueOf(categoryFilter.name())));


		return queryFactory.selectFrom(project)
				.where(builder)
				.orderBy(project.createdAt.desc())
				.fetch();
	}
}
