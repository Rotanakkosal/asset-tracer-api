package com.kshrd.asset_tracer_api.exception;

public class UnauthorizedExceptionHandler extends RuntimeException {
    public UnauthorizedExceptionHandler(String message) {
        super(message);
    }
}
