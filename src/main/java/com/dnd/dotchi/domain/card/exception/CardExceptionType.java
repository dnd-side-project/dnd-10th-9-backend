package com.dnd.dotchi.domain.card.exception;

import com.dnd.dotchi.global.exception.ExceptionType;
import lombok.Getter;

@Getter
public enum CardExceptionType implements ExceptionType {

    NOT_FOUND_CARD(500, "존재하지 않는 카드 ID입니다."),
    NOT_IMAGE(800, "유효하지 않은 이미지 정보입니다.");

    private final int code;

    private final String message;

    CardExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
