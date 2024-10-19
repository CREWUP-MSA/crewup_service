package com.example.projectservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Profile extends BaseTimeEntity {
	// TODO: profile image 추가 고려중

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	private Long id;

	@Column(name = "member_id", nullable = false, unique = true)
	private Long memberId;

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;

	@Column(name = "introduction")
	private String introduction;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<ProfileLink> links = new ArrayList<>();

	public void addLink(ProfileLink link) {
		this.links.add(link);
		link.setProfile(this);
	}
}
