package com.dnd.dotchi.domain.card.dto.response;

public record TodayCardsResponse(
	Long cardId,
	String cardImageUrl,
	String backName
){
}
