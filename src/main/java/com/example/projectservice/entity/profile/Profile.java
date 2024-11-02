package com.example.projectservice.entity.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.projectservice.dto.request.UpdateProfileRequest;
import com.example.projectservice.entity.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	public void update(UpdateProfileRequest request) {
		this.nickname = Optional.ofNullable(request.nickname()).orElse(this.nickname);
		this.introduction = Optional.ofNullable(request.introduction()).orElse(this.introduction);

		if (!request.links().isEmpty()){
			this.links.clear();
			request.links().forEach(link -> {
				addLink(ProfileLink.builder()
					.url(link.url())
					.linkType(link.linkType())
					.build());
			});
		}

	}
}
