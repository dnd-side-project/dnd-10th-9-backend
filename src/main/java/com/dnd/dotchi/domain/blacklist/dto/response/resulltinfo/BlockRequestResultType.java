package com.dnd.dotchi.domain.blacklist.dto.response.resulltinfo;

import lombok.Getter;

@Getter
public enum BlockRequestResultType {

    BLOCK_SUCCESS(1200, "차단이 성공하였습니다."),
    ALREADY_BLOCK(1201, "이미 차단하였습니다.");

    private final int code;
    private final String message;

    BlockRequestResultType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
