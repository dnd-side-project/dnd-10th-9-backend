package com.dnd.dotchi.domain.card.dto.response;

import java.time.LocalDateTime;

import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "홈페이지에 노출되는 테마들에 대한 응답")
public record ThemesOnHomeResponse(
	@Schema(description = "테마 ID")
	Long themeId,

	@Schema(description = "테마별 마지막으로 작성된 카드 시간")
	LocalDateTime lastCardCreateAt
) {

	public static ThemesOnHomeResponse from(final Card card) {
		return new ThemesOnHomeResponse(
			card.getTheme().getId(),
			card.getCreatedAt()
		);
	}

}
