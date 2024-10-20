package com.example.projectservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectservice.entity.ProjectMember;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
	Optional<ProjectMember> findByProjectIdAndMemberId(Long projectId, Long memberId);

	boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);
}
