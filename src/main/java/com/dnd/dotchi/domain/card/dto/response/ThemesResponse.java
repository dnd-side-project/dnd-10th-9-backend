package com.dnd.dotchi.domain.card.dto.response;

import java.time.LocalDateTime;

public record ThemesResponse(
	Long themeId,
	LocalDateTime lastCardCreateAt
) {
}
