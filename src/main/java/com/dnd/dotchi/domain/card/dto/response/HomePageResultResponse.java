package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈페이지 조회 결과 응답")
public record HomePageResultResponse(
	@Schema(description = "오늘의 인기 카드 목록")
	List<TodayCardsOnHomeResponse> todayCards,

	@Schema(description = "최신 카드 목록")
	List<RecentCardsOnHomeResponse> recentCards,

	@Schema(description = "각 테마별 최신 카드 작성 시간")
	List<ThemesOnHomeResponse> themes
) {
}
