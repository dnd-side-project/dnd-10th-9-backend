package com.dnd.dotchi.domain.member.exception;

import com.dnd.dotchi.global.exception.ExceptionType;

import lombok.Getter;

@Getter
public enum MemberExceptionType implements ExceptionType {

	NOT_FOUND_MEMBER(700, "존재하지 않는 회원 ID입니다.");

	private final int code;

	private final String message;

	MemberExceptionType(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
