package com.example.projectservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ProjectMember extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_member_id")
	private Long id;

	@Column(name = "member_id", nullable = false, unique = true)
	private Long memberId;

	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "position", nullable = false)
	@Enumerated(EnumType.STRING)
	private Position position;

	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "project_id")
	private Project project;

	//-- 연관관계 편의 메서드 --//
	public void setProject(Project project) {
		this.project = project;
	}
}
