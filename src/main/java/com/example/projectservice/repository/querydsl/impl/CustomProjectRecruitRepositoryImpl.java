package com.example.projectservice.repository.querydsl.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.projectservice.entity.project.ProjectRecruit;
import com.example.projectservice.entity.project.QProject;
import com.example.projectservice.entity.project.QProjectRecruit;
import com.example.projectservice.repository.querydsl.CustomProjectRecruitRepository;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomProjectRecruitRepositoryImpl implements CustomProjectRecruitRepository {

	private final JPQLQueryFactory queryFactory;

	/**
	 * 내가 지원한 프로젝트 목록 조회
	 * @param memberId 멤버 ID
	 * @return List<ProjectRecruit>
	 */
	@Override
	public List<ProjectRecruit> findMyRecruits(Long memberId) {
		QProject project = QProject.project;
		QProjectRecruit projectRecruit = QProjectRecruit.projectRecruit;

		return queryFactory.selectFrom(projectRecruit)
			.innerJoin(projectRecruit.project, project)
			.where(projectRecruit.memberId.eq(memberId))
			.orderBy(projectRecruit.createdAt.desc())
			.fetch();
	}
}
