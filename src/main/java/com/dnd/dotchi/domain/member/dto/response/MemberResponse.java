package com.dnd.dotchi.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보")
public record MemberResponse(
        @Schema(description = "회원 ID")
        Long id,
        
        @Schema(description = "회원 닉네임")
        String memberName,

        @Schema(description = "회원 이미지 URL")
        String memberImageUrl,

        @Schema(description = "회원 소개글")
        String description,

        @Schema(description = "회원의 카드 개수")
        Long cardCount
) {

        public static MemberResponse of(
                final long id,
                final String memberName,
                final String memberImageUrl,
                final String description,
                final long cardCount
        ) {
                return new MemberResponse(id, memberName, memberImageUrl, description, cardCount);
        }

}
