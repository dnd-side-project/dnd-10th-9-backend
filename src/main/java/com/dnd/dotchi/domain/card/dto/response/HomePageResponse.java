package com.dnd.dotchi.domain.card.dto.response;

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
}