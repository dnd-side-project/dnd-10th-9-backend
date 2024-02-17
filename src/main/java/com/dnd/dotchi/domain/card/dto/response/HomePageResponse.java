package com.dnd.dotchi.domain.card.dto.response;

import java.util.List;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import com.dnd.dotchi.domain.card.entity.TodayCard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메인 홈페이지에 대한 응답")
public record HomePageResponse(
	@Schema(description = "응답 코드", example = "100")
	Integer code,

	@Schema(description = "응답 상태 메시지", example = "요청에 성공하였습니다.")
	String message,

	@Schema(description = "응답 결과")
	HomePageResultResponse result
){

	public static HomePageResponse of(
		final CardsRequestResultType resultType,
		final List<TodayCard> todayCards,
		final List<Card> recentCards,
		final List<Card> recentCardsByThemes
	) {
		return new HomePageResponse(
			resultType.getCode(),
			resultType.getMessage(),
			HomePageResultResponse.of(
				todayCards,
				recentCards,
				recentCardsByThemes
			)
		);
	}

}