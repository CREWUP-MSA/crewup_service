package com.example.projectservice.repository.querydsl.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.projectservice.entity.Project;
import com.example.projectservice.entity.QProject;
import com.example.projectservice.entity.QProjectMember;
import com.example.projectservice.repository.querydsl.CustomProjectRepository;
import com.querydsl.jpa.JPQLQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomProjectRepositoryImpl implements CustomProjectRepository {

	private final JPQLQueryFactory queryFactory;

	public List<Project> findMyProjects(Long memberId) {
		QProject project = QProject.project;
		QProjectMember projectMember = QProjectMember.projectMember;

		return queryFactory.selectFrom(project)
				.innerJoin(project.members, projectMember)
				.where(projectMember.memberId.eq(memberId))
				.fetch();
	}

}
