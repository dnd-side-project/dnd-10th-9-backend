package com.dnd.dotchi.domain.card.exception;

import com.dnd.dotchi.global.exception.ExceptionType;

import lombok.Getter;

@Getter
public enum ThemeExceptionType implements ExceptionType {

	NOT_FOUND_THEME(600, "존재하지 않는 테마 ID입니다.");

	private final int code;

	private final String message;

	ThemeExceptionType(int code, String message) {
		this.code = code;
		this.message = message;
	}

}
