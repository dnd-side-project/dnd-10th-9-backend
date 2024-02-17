package com.dnd.dotchi.domain.member.controller;

import com.dnd.dotchi.domain.member.dto.request.MemberAuthorizationRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberInfoRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(
            @PathVariable("memberId") final Long memberId,
            @Valid @ModelAttribute final MemberInfoRequest request
    ) {
        final MemberInfoResponse response = memberService.getMemberInfo(memberId, request.lastCardId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberAuthorizationResponse> login(
            @Valid @RequestBody final MemberAuthorizationRequest request
    ) {
        final MemberAuthorizationResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

}
