package com.example.projectservice.repository.querydsl;

import java.util.List;

import com.example.projectservice.dto.request.CategoryFilter;
import com.example.projectservice.dto.request.Filter;
import com.example.projectservice.entity.Position;
import com.example.projectservice.entity.Project;

public interface CustomProjectRepository {
	List<Project> findMyProjects(Long memberId);
	List<Project> findProjectsByFilter(Filter filter, Position position, CategoryFilter categoryFilter);
}
