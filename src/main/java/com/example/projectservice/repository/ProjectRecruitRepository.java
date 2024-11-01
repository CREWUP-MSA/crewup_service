package com.example.projectservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectservice.entity.project.ProjectRecruit;

public interface ProjectRecruitRepository extends JpaRepository<ProjectRecruit, Long> {
	List<ProjectRecruit> findByMemberId(Long memberId);

	List<ProjectRecruit> findByProjectId(Long projectId);
}
