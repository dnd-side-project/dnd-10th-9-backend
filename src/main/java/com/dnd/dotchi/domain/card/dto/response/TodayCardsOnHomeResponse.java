package com.dnd.dotchi.domain.card.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈페이지에 노출되는 오늘의 카드들에 대한 응답")
public record TodayCardsOnHomeResponse(
	@Schema(description = "카드 ID")
	Long cardId,

	@Schema(description = "카드 이미지 URL")
	String cardImageUrl,

	@Schema(description = "카드 이름")
	String backName
){
}
