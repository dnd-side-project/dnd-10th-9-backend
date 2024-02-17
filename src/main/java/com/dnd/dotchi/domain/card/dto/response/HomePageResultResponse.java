package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;
import com.dnd.dotchi.domain.member.entity.Member;

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

	public static HomePageResultResponse of(
		final List<TodayCard> todayCards,
		final List<Card> recentCards,
		final List<Card> recentCardsByThemes
	) {
		return new HomePageResultResponse(
			parseTodayCardsResponse(todayCards),
			parseRecentCardsResponse(recentCards),
			parseRecentCardsByThemesResponse(recentCardsByThemes)
		);
	}

	private static List<TodayCardsOnHomeResponse> parseTodayCardsResponse(final List<TodayCard> todayCards) {
		return todayCards.stream()
			.map(TodayCardsOnHomeResponse::from)
			.toList();
	}

	private static List<RecentCardsOnHomeResponse> parseRecentCardsResponse(final List<Card> recentCards) {
		return recentCards.stream()
			.map(RecentCardsOnHomeResponse::from)
			.toList();
	}

	private static List<ThemesOnHomeResponse> parseRecentCardsByThemesResponse(final List<Card> recentCards) {
		return recentCards.stream()
			.map(ThemesOnHomeResponse::from)
			.toList();
	}

}
