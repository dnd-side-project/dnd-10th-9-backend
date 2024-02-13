package com.dnd.dotchi.domain.card.dto.response;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsByThemeRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "테마별 카드 조회 응답")
public record CardsByThemeResponse(
        @Schema(description = "응답 코드", example = "100")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "100")
        String message,

        @Schema(description = "테마별 카드 조회 결과")
        CardsByThemeResultResponse result
) {

        public static CardsByThemeResponse of(
                final CardsByThemeRequestResultType resultType,
                final List<Card> cardsByTheme
        ) {
                return new CardsByThemeResponse(
                        resultType.getCode(),
                        resultType.getMessage(),
                        CardsByThemeResultResponse.from(cardsByTheme)
                );
        }

}
