package com.dnd.dotchi.domain.card.exception;

import com.dnd.dotchi.global.exception.ExceptionType;
import lombok.Getter;

@Getter
public enum CardExceptionType implements ExceptionType {

    NOT_FOUND_CARD(500, "존재하지 않는 카드 ID입니다."),
    NOT_CARD_WRITER(501, "본인이 작성한 카드만 삭제할 수 있습니다."),
    NOT_IMAGE(800, "유효하지 않은 이미지 정보입니다."),
    WRITE_COMMENT_ON_CARD_FAILURE(1051, "댓글 작성에 실패했습니다. 다시 시도해주세요."),
    MY_COMMENT_ALREADY_EXIST(1052, "이미 댓글을 작성한 카드입니다.");

    private final int code;

    private final String message;

    CardExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
