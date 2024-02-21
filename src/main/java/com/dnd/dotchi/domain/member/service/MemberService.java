package com.dnd.dotchi.domain.member.service;

import java.util.List;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.member.dto.request.MemberAuthorizationRequest;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.jwt.TokenProcessor;
import com.dnd.dotchi.infra.image.S3FileUploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private static final String EMPTY_FILE_START_NAME = "34f2743d-a9";

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final TokenProcessor tokenProcessor;
    private final S3FileUploader s3FileUploader;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(final Member member, final Long lastCardId) {

        final List<Card> recentCardsByMember =
                cardRepository.findCardsByMemberWithFilteringAndPaging(member.getId(), lastCardId);

        return MemberInfoResponse.of(MemberRequestResultType.GET_MEMBER_INFO_SUCCESS, member, recentCardsByMember);
    }

    @Transactional(readOnly = true)
    public MemberAuthorizationResponse login(final MemberAuthorizationRequest request) {
        final Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
        final String accessToken = tokenProcessor.generateAccessToken(member.getId());

        return MemberAuthorizationResponse.of(MemberRequestResultType.LOGIN_SUCCESS, member, accessToken);
    }

    @Transactional(readOnly = true)
    public Member findById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));
    }

    public MemberModifyResponse patchMemberInfo(final Member member, final MemberModifyRequest request) {
        final Optional<MultipartFile> multipartFile = request.memberImage();
        if(isModifyProfileImage(multipartFile)) {
            final MultipartFile image = multipartFile.get();
            final String fileFullPath = s3FileUploader.upload(image);
            member.setImageUrl(fileFullPath);
        }
        member.setNickname(request.memberName());
        member.setDescription(request.memberDescription());

        return MemberModifyResponse.of(MemberRequestResultType.PATCH_MEMBER_INFO_SUCCESS);
    }

    private boolean isModifyProfileImage(final Optional<MultipartFile> multipartFile) {
        return multipartFile.isPresent()
                && !Objects.requireNonNull(multipartFile.get().getOriginalFilename()).startsWith(EMPTY_FILE_START_NAME);
    }

}
