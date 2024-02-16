package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 조회 응답")
public record GetCommentOnCardResponse(
        @Schema(description = "응답 코드", example = "1040")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "요청에 성공하였습니다.")
        String message
) {
}
