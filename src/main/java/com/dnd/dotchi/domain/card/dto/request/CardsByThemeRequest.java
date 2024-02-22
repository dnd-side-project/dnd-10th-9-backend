package com.dnd.dotchi.domain.card.dto.request;

import com.dnd.dotchi.domain.card.entity.vo.CardSortType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "테마별 카드 조회 요청")
public record CardsByThemeRequest(
        @Schema(description = "테마 ID", example = "1")
        @NotNull(message = "테마 ID는 빈 값일 수 없습니다.")
        @Positive(message = "테마 ID는 양수만 가능합니다.")
        Long themeId,

        @Schema(description = "카드 정렬 타입", example = "LATEST")
        @NotNull(message = "카드 정렬 타입은 빈 값일 수 없습니다.")
        CardSortType cardSortType,

        @Schema(description = "마지막 조회 카드 ID", example = "99999")
        @NotNull(message = "마지막 조회 카드 ID는 빈 값일 수 없습니다.")
        @Positive(message = "마지막 조회 가드 ID는 양수만 가능합니다.")
        Long lastCardId,

        @Schema(description = "마지막 조회 카드의 댓글 개수", example = "99999")
        @NotNull(message = "마지막 조회 카드의 댓글 개수는 빈 값일 수 없습니다.")
        @Positive(message = "마지막 조회 카드의 댓글 개수는 양수만 가능합니다.")
        Long lastCardCommentCount
) {
}
