package com.dnd.dotchi.domain.card.dto.response.resultinfo;

import lombok.Getter;

@Getter
public enum CardsRequestResultType {

    GET_CARDS_BY_THEME_SUCCESS(1020, "테마별 카드 조회 요청에 성공하였습니다."),
    WRITE_CARDS_SUCCESS(1030, "카드 작성에 성공하였습니다."),
    WRITE_COMMENT_ON_CARD_SUCCESS(1050, "댓글 작성에 성공하였습니다.");

    private final int code;
    private final String message;

    CardsRequestResultType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
