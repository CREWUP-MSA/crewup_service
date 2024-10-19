package com.example.projectservice.repository.querydsl;

import java.util.List;

import com.example.projectservice.entity.Project;

public interface CustomProjectRepository {
	List<Project> findMyProjects(Long memberId);
}
