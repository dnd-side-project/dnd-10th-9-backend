package com.dnd.dotchi.global.exception;

public class RetryLimitExceededException extends BaseException {

    public RetryLimitExceededException(final ExceptionType exceptionType) {
        super(exceptionType);
    }

}
