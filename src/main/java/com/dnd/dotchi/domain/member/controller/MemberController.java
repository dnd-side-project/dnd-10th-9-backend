package com.dnd.dotchi.domain.member.controller;

import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.dotchi.domain.card.exception.CardExceptionType;
import com.dnd.dotchi.domain.member.dto.request.MemberAuthorizationRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberInfoRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.service.MemberService;
import com.dnd.dotchi.global.exception.BadRequestException;
import com.dnd.dotchi.global.jwt.Auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberInfoResponse> getMemberInfo(
            @Auth final Member member,
            @Valid @ModelAttribute final MemberInfoRequest request
    ) {
        final MemberInfoResponse response = memberService.getMemberInfo(member, request.lastCardId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberAuthorizationResponse> login(
            @Valid @RequestBody final MemberAuthorizationRequest request
    ) {
        final MemberAuthorizationResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
        value = "/me"
    )
    public ResponseEntity<MemberModifyResponse> patchMemberInfo(
        @Auth final Member member,
        @Valid @ModelAttribute final MemberModifyRequest request
    ) {
        validMultiPartFileisImage(request.memberImage());

        final MemberModifyResponse response = memberService.patchMemberInfo(member, request);
        return ResponseEntity.ok(response);
    }

    private void validMultiPartFileisImage(final Optional<MultipartFile> imageOpt) {
        if(imageOpt.isPresent()) {
            final MultipartFile image = imageOpt.get();
            if (!image.getContentType().startsWith("image")) {
                throw new BadRequestException(CardExceptionType.NOT_IMAGE);
            }
        }
    }

}
