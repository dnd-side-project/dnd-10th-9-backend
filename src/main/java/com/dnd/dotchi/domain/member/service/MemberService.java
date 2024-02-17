package com.dnd.dotchi.domain.member.service;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.member.dto.request.MemberAuthorizationRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberAuthorizationResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
import com.dnd.dotchi.global.jwt.TokenProcessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final TokenProcessor tokenProcessor;

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(final Long memberId, final Long lastCardId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));

        final List<Card> recentCardsByMember =
                cardRepository.findCardsByMemberWithFilteringAndPaging(memberId, lastCardId);

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

}
