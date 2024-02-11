package com.dnd.dotchi.global.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

}
