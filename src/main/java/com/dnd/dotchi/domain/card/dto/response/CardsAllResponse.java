package com.dnd.dotchi.domain.card.dto.response;


import java.util.List;

import com.dnd.dotchi.domain.card.dto.response.resultinfo.CardsRequestResultType;
import com.dnd.dotchi.domain.card.entity.Card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "전체 카드 조회 응답")
public record CardsAllResponse(
        @Schema(description = "응답 코드", example = "1010")
        Integer code,

        @Schema(description = "응답 상태 메시지", example = "전체 카드 조회 요청에 성공하였습니다.")
        String message,

        @Schema(description = "전체 카드 조회 결과")
        CadsAllResultResponse result
) {

        public static CardsAllResponse of(
            final CardsRequestResultType resultType,
            final List<Card> cards
        ) {
                return new CardsAllResponse(
                    resultType.getCode(),
                    resultType.getMessage(),
                    CadsAllResultResponse.from(cards)
                );
        }

}
