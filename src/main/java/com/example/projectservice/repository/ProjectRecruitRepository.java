package com.example.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectservice.entity.project.ProjectRecruit;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRecruitRepository extends JpaRepository<ProjectRecruit, Long> {
	@Query("SELECT pr " +
			"FROM ProjectRecruit pr " +
			"JOIN FETCH pr.project " +
			"WHERE pr.memberId = :memberId")
	List<ProjectRecruit> findByMemberId(Long memberId);

	List<ProjectRecruit> findByProjectId(Long projectId);

	boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
