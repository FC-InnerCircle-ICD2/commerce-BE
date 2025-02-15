package com.emotionalcart.auth.infrasturcture.member.http;

import com.emotionalcart.auth.application.dto.MemberRequest;
import com.emotionalcart.auth.application.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service", url = "${member.find.feign-endpoint}", path = "/api/v1/members")
public interface MemberFeignClient {

    @PostMapping(value = "/find-or-create", consumes = MediaType.APPLICATION_JSON_VALUE)
    MemberResponse findOrCreate(@RequestBody MemberRequest request);
}
