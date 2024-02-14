package com.dnd.dotchi.domain.card.dto.response.resultinfo;

import lombok.Getter;

@Getter
public enum CardsByThemeRequestResultType {
    SUCCESS(1020, "요청에 성공하였습니다.");

    private final int code;
    private final String message;

    CardsByThemeRequestResultType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
