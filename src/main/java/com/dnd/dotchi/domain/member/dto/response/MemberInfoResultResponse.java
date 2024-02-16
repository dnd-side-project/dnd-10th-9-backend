package com.dnd.dotchi.domain.member.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "회원 정보 조회 결과 응답")
public record MemberInfoResultResponse(
        @Schema(description = "회원 정보")
        MemberResponse member,

        @Schema(description = "회원의 최신순 카드 목록")
        List<RecentCardsByMemberResponse> recentCards
) {

        public static MemberInfoResultResponse of(final Member member, final List<Card> recentCardsByMember) {
                final List<RecentCardsByMemberResponse> recentCards = recentCardsByMember.stream()
                        .map(RecentCardsByMemberResponse::from)
                        .toList();
                return new MemberInfoResultResponse(MemberResponse.from(member), recentCards);
        }

}
