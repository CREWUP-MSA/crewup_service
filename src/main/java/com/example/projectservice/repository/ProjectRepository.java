package com.example.projectservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectservice.entity.project.Project;
import com.example.projectservice.repository.querydsl.CustomProjectRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, CustomProjectRepository {
}
