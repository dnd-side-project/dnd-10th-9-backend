package com.dnd.dotchi.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원의 최신순 카드 응답")
public record RecentCardsByMemberResponse(
        @Schema(description = "카드 ID")
        Long cardId,

        @Schema(description = "해당 카드를 작성한 회원 ID")
        Long memberId,

        @Schema(description = "해당 카드를 작성한 회원 닉네임")
        String memberName,

        @Schema(description = "카드 이미지 URL")
        String cardImageUrl,

        @Schema(description = "테마 ID")
        Long themeId,

        @Schema(description = "카드 이름")
        String backName
) {
}
