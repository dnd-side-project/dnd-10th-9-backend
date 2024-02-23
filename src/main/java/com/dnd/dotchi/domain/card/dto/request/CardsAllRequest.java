package com.dnd.dotchi.domain.card.dto.request;

import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "전체 카드 조회하기")
public record CardsAllRequest(
        @Schema(description = "카드 정렬 타입", example = "LATEST")
        @NotNull(message = "카드 정렬 타입은 빈 값일 수 없습니다.")
        CardSortType cardSortType,

        @Schema(description = "마지막으로 조회한 카드 ID", example = "99999")
        @NotNull(message = "마지막으로 조회한 카드 ID는 빈 값일 수 없습니다.")
        @Positive(message = "마지막으로 조회한 카드 ID는 양수만 가능합니다.")
        Long lastCardId,

        @Schema(description = "마지막으로 조회한 카드의 댓글 수", example = "99999")
        @NotNull(message = "마지막으로 조회한 카드의 댓글 수는 빈 값일 수 없습니다.")
        @PositiveOrZero(message = "마지막으로 조회한 카드의 댓글 수는 0 이상만 가능합니다.")
        Long lastCardCommentCount
) {
}
