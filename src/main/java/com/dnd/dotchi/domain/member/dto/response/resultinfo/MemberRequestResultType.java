package com.dnd.dotchi.domain.member.dto.response.resultinfo;

import lombok.Getter;

@Getter
public enum MemberRequestResultType {

    PATCH_MEMBER_INFO_SUCCESS(1120, "회원 정보 수정에 성공하였습니다."),

    GET_MEMBER_INFO_SUCCESS(1110, "회원 정보 요청에 성공하였습니다.");

    private final int code;
    private final String message;

    MemberRequestResultType(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
    
}
