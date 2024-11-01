package com.example.projectservice.entity.project;

import com.example.projectservice.dto.request.UpdateMemberToProject;
import com.example.projectservice.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@Column(name = "role", nullable = false)
	private Role role;

	@Column(name = "position", nullable = false)
	@Enumerated(EnumType.STRING)
	private Position position;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	//-- 연관관계 편의 메서드 --//
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * 프로젝트 멤버 수정
	 * @param request 수정할 프로젝트 멤버 정보
	 */
	public void update(UpdateMemberToProject request) {
		this.position = request.position();
	}

	/**
	 * 리더 변경
	 * @param member 리더로 변경할 멤버
	 */
	public void updateLeader(ProjectMember member) {
		this.role = Role.MEMBER;
		member.role = Role.LEADER;
	}
}
