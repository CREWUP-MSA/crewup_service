package com.example.projectservice.dto.response;

import java.util.List;

import com.example.projectservice.entity.profile.Profile;

import lombok.Builder;

@Builder
public record ProfileResponse(
	Long id,
	String nickname,
	String introduction,
	// String profileImage,
	List<LinkResponse> links
) {
	public static ProfileResponse from(Profile profile) {
		return ProfileResponse.builder()
			.id(profile.getId())
			.nickname(profile.getNickname())
			.introduction(profile.getIntroduction())
			.links(profile.getLinks().stream()
				.map(LinkResponse::from)
				.toList())
			.build();
	}
}
