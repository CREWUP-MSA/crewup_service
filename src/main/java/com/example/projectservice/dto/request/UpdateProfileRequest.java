package com.example.projectservice.dto.request;

import java.util.List;

public record UpdateProfileRequest(
	String nickname,
	String introduction,
	List<ProfileLinkRequest> links
) {
}
