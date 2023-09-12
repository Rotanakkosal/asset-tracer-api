package com.kshrd.asset_tracer_api.exception;

public class InvalidPasswordExceptionHandler extends RuntimeException {
    public InvalidPasswordExceptionHandler(String message) {
        super(message);
    }
}
