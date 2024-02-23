package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "해당 테마의 최신순 카드 응답")
public record RecentCardsByThemeResponse(
        @Schema(description = "카드 ID")
        Long cardId,

        @Schema(description = "해당 카드를 작성한 회원 ID")
        Long memberId,

        @Schema(description = "해당 카드를 작성한 회원 닉네임")
        String memberName,

        @Schema(description = "해당 카드를 작성한 회원 프로필 이미지 URL")
        String memberImageUrl,

        @Schema(description = "카드 이미지 URL")
        String cardImageUrl,

        @Schema(description = "테마 ID")
        Long themeId,

        @Schema(description = "카드 이름")
        String backName,

        @Schema(description = "카드 댓글 개수")
        Long commentCount
) {

    public static RecentCardsByThemeResponse from(final Card card) {
        return new RecentCardsByThemeResponse(
                card.getId(),
                card.getMember().getId(),
                card.getMember().getNickname(),
                card.getMember().getImageUrl(),
                card.getImageUrl(),
                card.getTheme().getId(),
                card.getBackName(),
                card.getCommentCount()
        );
    }

}
