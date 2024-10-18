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
public class ProfileLink extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_link_id")
	private Long id;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "link_type")
	@Enumerated(EnumType.STRING)
	private LinkType linkType;

	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "profile_id")
	private Profile profile;

	//-- 연관관계 편의 메서드 --//
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
