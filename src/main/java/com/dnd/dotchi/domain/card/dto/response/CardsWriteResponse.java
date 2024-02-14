package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "테마별 카드 조회 응답")
public record CardsWriteResponse(
	@Schema(description = "응답 코드", example = "1030")
	Integer code,

	@Schema(description = "응답 상태 메시지", example = "성공")
	String message
) {

	public static CardsWriteResponse of(final CardsRequestResultType resultType) {
		return new CardsWriteResponse(
			resultType.getCode(),
			resultType.getMessage()
		);
	}

}