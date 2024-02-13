package com.dnd.dotchi.domain.card.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "테마별 카드 조회 응답")
public record CardsByThemeResponse(
        @Schema(description = "응답 코드", example = "100")
        Long code,

        @Schema(description = "응답 상태 메시지", example = "100")
        String message,

        @Schema(description = "테마별 카드 조회 결과")
        CardsByThemeResult result
) {
}
