package com.dnd.dotchi.domain.card.dto.response;

public record RecentCardsResponse(
	Long cardId,
	Long memberId,
	String memberName,
	String cardImageUrl,
	Long themeId,
	String backName
) {
}
