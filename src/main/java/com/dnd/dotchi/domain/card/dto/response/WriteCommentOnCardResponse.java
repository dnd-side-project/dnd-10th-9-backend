package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 작성 응답")
public record WriteCommentOnCardResponse(
        @Schema(description = "응답 코드", example = "100")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "100")
        String message
) {

    public static WriteCommentOnCardResponse from(final CardsRequestResultType resultType) {
        return new WriteCommentOnCardResponse(resultType.getCode(), resultType.getMessage());
    }

}
