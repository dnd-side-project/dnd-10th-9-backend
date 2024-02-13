package com.dnd.dotchi.domain.card.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "테마별 카드 조회 결과 응답")
public record CardsByThemeResult(
        @Schema(description = "해당 테마의 최신순 카드 목록")
        List<RecentCardsByThemeResponse> recentCards
) {
}
