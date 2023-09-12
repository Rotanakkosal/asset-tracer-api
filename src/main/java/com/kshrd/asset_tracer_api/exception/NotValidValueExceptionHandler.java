package com.kshrd.asset_tracer_api.exception;

public class NotValidValueExceptionHandler extends RuntimeException {
    public NotValidValueExceptionHandler(String message) {
        super(message);
    }
}
