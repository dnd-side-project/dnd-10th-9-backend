package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈페이지에 노출되는 최신 카드들에 대한 응답")
public record RecentCardsOnHomeResponse(
	@Schema(description = "카드 ID")
	Long cardId,

	@Schema(description = "카드를 작성한 회원 ID")
	Long memberId,

	@Schema(description = "카드를 작성한 회원 이름")
	String memberName,

	@Schema(description = "카드 이미지 URL")
	String cardImageUrl,

	@Schema(description = "카드 테마 ID")
	Long themeId,

	@Schema(description = "카드 이름")
	String backName
) {

	public static RecentCardsOnHomeResponse from(final Card card) {
		return new RecentCardsOnHomeResponse(
			card.getId(),
			card.getMember().getId(),
			card.getMember().getNickname(),
			card.getImageUrl(),
			card.getTheme().getId(),
			card.getBackName()
		);
	}

}
