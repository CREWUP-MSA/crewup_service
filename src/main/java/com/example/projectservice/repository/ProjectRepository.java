package com.example.projectservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectservice.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
