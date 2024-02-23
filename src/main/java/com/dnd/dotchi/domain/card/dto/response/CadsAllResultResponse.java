package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 카드 최신순/인기순 응답")
public record CadsAllResultResponse(
	@Schema(description = "전체 카드 최신순/인기순 응답")
	List<CardsResponse> cards
) {
	public static CadsAllResultResponse from(final List<Card> cards) {
		return new CadsAllResultResponse(parseRecentCardsAllResponse(cards));
	}

	private static List<CardsResponse> parseRecentCardsAllResponse(final List<Card> cards) {
		return cards.stream()
			.map(CardsResponse::from)
			.toList();
	}
}
