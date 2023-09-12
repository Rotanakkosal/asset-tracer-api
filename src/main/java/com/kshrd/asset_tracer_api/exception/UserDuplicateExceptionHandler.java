package com.kshrd.asset_tracer_api.exception;

public class UserDuplicateExceptionHandler extends RuntimeException {
    public UserDuplicateExceptionHandler(String message) {
        super(message);
    }
}
