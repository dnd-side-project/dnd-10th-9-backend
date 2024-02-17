package com.dnd.dotchi.domain.member.service;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.repository.CardRepository;
import com.dnd.dotchi.domain.member.dto.request.MemberModifyRequest;
import com.dnd.dotchi.domain.member.dto.response.MemberInfoResponse;
import com.dnd.dotchi.domain.member.dto.response.MemberModifyResponse;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import com.dnd.dotchi.domain.member.exception.MemberExceptionType;
import com.dnd.dotchi.domain.member.repository.MemberRepository;
import com.dnd.dotchi.global.exception.NotFoundException;
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

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(final Long memberId, final Long lastCardId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(MemberExceptionType.NOT_FOUND_MEMBER));

        final List<Card> recentCardsByMember =
                cardRepository.findCardsByMemberWithFilteringAndPaging(memberId, lastCardId);

        return MemberInfoResponse.of(MemberRequestResultType.GET_MEMBER_INFO_SUCCESS, member, recentCardsByMember);
    }

    @Transactional
    public MemberModifyResponse patchMemberInfo(MemberModifyRequest request) {
        return null;
    }
}
