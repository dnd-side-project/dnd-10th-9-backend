package com.dnd.dotchi.domain.member.dto.response;

import com.dnd.dotchi.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberAuthorizationResultResponse(
        @Schema(description = "회원 ID")
        Long memberId,

        @Schema(description = "회원 닉네임")
        String memberName,

        @Schema(description = "회원 이미지 URL")
        String memberImageUrl,

        @Schema(description = "인증 토큰", example = "abc.def.ghi")
        String accessToken
) {

    public static MemberAuthorizationResultResponse of(final Member member, final String accessToken) {
        return new MemberAuthorizationResultResponse(
                member.getId(),
                member.getNickname(),
                member.getImageUrl(),
                accessToken
        );
    }

}
