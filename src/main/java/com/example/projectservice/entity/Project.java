package com.example.projectservice.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
public class Project extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	@Column(name = "project_title", nullable = false)
	private String title;

	@Column(name = "project_content", nullable = false)
	private String content;

	@ElementCollection(targetClass = Position.class)
	@CollectionTable(name = "project_need_positions", joinColumns = @JoinColumn(name = "project_id"))
	@Column(name = "need_position")
	@Enumerated(EnumType.STRING)
	private List<Position> needPositions;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectMember> members;

	/**
	 * 프로젝트 멤버 추가
	 * @param member 추가할 프로젝트 멤버
	 */
	public void addMember(ProjectMember member) {
		this.members.add(member);
		member.setProject(this);
	}
}