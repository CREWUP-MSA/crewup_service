package com.example.projectservice.repository.querydsl;

import java.util.List;

import com.example.projectservice.entity.project.ProjectRecruit;

public interface CustomProjectRecruitRepository {

	List<ProjectRecruit> findMyRecruits(Long memberId);
}
