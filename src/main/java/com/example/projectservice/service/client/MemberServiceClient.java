package com.example.projectservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.projectservice.dto.ClientResponse;
import com.example.projectservice.dto.client.MemberResponse;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

	@GetMapping("/member-service/api/member/by-id")
	ClientResponse<MemberResponse> getMemberById(@RequestParam("id") Long id);
}
