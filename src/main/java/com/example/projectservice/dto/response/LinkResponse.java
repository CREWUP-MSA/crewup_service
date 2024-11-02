package com.example.projectservice.dto.response;

import com.example.projectservice.entity.profile.LinkType;
import com.example.projectservice.entity.profile.ProfileLink;

import lombok.Builder;

@Builder
public record LinkResponse(
	LinkType linkType,
	String url
) {
	public static LinkResponse from(ProfileLink links) {
		return LinkResponse.builder()
			.linkType(links.getLinkType())
			.url(links.getUrl())
			.build();
	}
}
