package com.dnd.dotchi.domain.member.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.member.dto.response.resultinfo.MemberRequestResultType;
import com.dnd.dotchi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "회원 정보 조회 응답")
public record MemberInfoResponse(
        @Schema(description = "응답 코드", example = "1110")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "회원 정보 요청에 성공하였습니다.")
        String message,
        
        @Schema(description = "회원 정보 조회 결과")
        MemberInfoResultResponse result
) {

        public static MemberInfoResponse of(
                final MemberRequestResultType resultType,
                final Member member,
                final List<Card> recentCardsByMember
        ) {
                final MemberInfoResultResponse resultResponse =
                        MemberInfoResultResponse.of(member, recentCardsByMember);
                return new MemberInfoResponse(resultType.getCode(), resultType.getMessage(), resultResponse);
        }

}
