package com.example.projectservice.entity;

import java.util.*;

import com.example.projectservice.dto.request.UpdateProjectRequest;
import com.example.projectservice.exception.CustomException;
import com.example.projectservice.exception.ErrorCode;
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

	@Column(name = "project_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ElementCollection(targetClass = Position.class)
	@CollectionTable(name = "project_need_positions", joinColumns = @JoinColumn(name = "project_id"))
	@Column(name = "need_position")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Set<Position> needPositions = new HashSet<>();

	@ElementCollection(targetClass = Category.class)
	@CollectionTable(name = "project_categories", joinColumns = @JoinColumn(name = "project_id"))
	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Set<Category> categories = new HashSet<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProjectMember> members = new ArrayList<>();

	/**
	 * 프로젝트 멤버 추가
	 * @param member 추가할 프로젝트 멤버
	 */
	public void addMember(ProjectMember member) {
		this.members.add(member);
		member.setProject(this);
	}

	/**
	 * 프로젝트 정보 수정
	 * @param request 수정할 프로젝트 정보
	 */
	public void update(UpdateProjectRequest request) {
		this.title = Optional.ofNullable(request.title()).orElse(this.title);
		this.content = Optional.ofNullable(request.content()).orElse(this.content);
		this.needPositions = Optional.ofNullable(request.needPositions()).orElse(this.needPositions);
		this.categories = Optional.ofNullable(request.categories()).orElse(this.categories);
	}

	/**
	 * 프로젝트 완료 처리
	 * @throws CustomException 이미 완료된 프로젝트일 경우
	 */
    public void complete() {
		if (this.status == Status.COMPLETED) {
			throw new CustomException(ErrorCode.ALREADY_COMPLETED_PROJECT);
		}
		this.status = Status.COMPLETED;
    }

	public boolean isLeader(Long requesterId) {
		return this.members.stream()
			.anyMatch(member -> member.getMemberId().equals(requesterId) && member.getRole().equals(Role.LEADER));
	}

	/**
	 * 프로젝트 멤버 여부 확인
	 * @param requesterId 요청자 ID
	 * @return 프로젝트 멤버 여부
	 */
	public boolean isMember(Long requesterId) {
		return this.members.stream()
			.anyMatch(member -> member.getMemberId().equals(requesterId));
	}
}
