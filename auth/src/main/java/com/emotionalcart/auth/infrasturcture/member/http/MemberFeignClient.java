package com.emotionalcart.auth.infrasturcture.member.http;

import com.emotionalcart.auth.application.dto.MemberRequest;
import com.emotionalcart.auth.application.dto.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-service", url = "${member.find.feign-endpoint}", path = "/v1/members")
public interface MemberFeignClient {

    @PostMapping("/find-or-create")
    MemberResponse findOrCreate(@RequestBody MemberRequest request);
}
