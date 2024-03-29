package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "카드 정보에 대한 응답")
public record CardsResponse(
        @Schema(description = "카드 ID")
        Long cardId,

        @Schema(description = "카드를 작성한 회원 ID")
        Long memberId,

        @Schema(description = "카드를 작성한 회원 닉네임")
        String memberName,

        @Schema(description = "카드를 작성한 회원 프로필 이미지 URL")
        String memberImageUrl,

        @Schema(description = "카드 이미지 URL")
        String cardImageUrl,

        @Schema(description = "카드의 테마 ID")
        Long themeId,

        @Schema(description = "카드 이름")
        String backName,

        @Schema(description = "카드 기분")
        String backMood,

        @Schema(description = "카드 중간 문장")
        String backContent,

        @Schema(description = "카드의 댓글 수")
        Long commentCount
) {

    public static CardsResponse from(final Card card) {
        return new CardsResponse(
                card.getId(),
                card.getMember().getId(),
                card.getMember().getNickname(),
                card.getMember().getImageUrl(),
                card.getImageUrl(),
                card.getTheme().getId(),
                card.getBackName(),
                card.getBackMood(),
                card.getBackContent(),
                card.getCommentCount()
        );
    }

}
