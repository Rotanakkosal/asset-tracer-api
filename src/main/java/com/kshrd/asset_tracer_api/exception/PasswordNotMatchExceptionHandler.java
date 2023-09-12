package com.kshrd.asset_tracer_api.exception;

public class PasswordNotMatchExceptionHandler extends RuntimeException {
    public PasswordNotMatchExceptionHandler(String message) {
        super(message);
    }
}
