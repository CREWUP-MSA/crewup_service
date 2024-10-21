package com.example.projectservice.dto.response;

import java.util.List;

import com.example.projectservice.entity.LinkType;
import com.example.projectservice.entity.ProfileLink;

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
