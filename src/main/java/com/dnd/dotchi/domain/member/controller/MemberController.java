package com.dnd.dotchi.domain.member.controller;

import com.dnd.dotchi.domain.member.dto.request.MemberInfoRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping(
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
        value = "/me"
    )
    public ResponseEntity<MemberModifyResponse> patchMemberInfo(
        @Valid @ModelAttribute final MemberModifyRequest request
    ) {
        final MemberModifyResponse response = memberService.patchMemberInfo(request);
        return null;
    }

}
