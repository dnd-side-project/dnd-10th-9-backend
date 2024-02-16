package com.dnd.dotchi.domain.card.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record HomePageResponse(
	Integer code,

	String message,

	CommentResultResponse result
){
}