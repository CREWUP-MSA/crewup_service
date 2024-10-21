package com.example.projectservice.dto.request;

import com.example.projectservice.entity.LinkType;

public record ProfileLinkRequest(
	LinkType linkType,
	String url
) {
}
