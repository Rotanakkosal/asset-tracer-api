package com.kshrd.asset_tracer_api.exception;

public class FieldBlankExceptionHandler extends RuntimeException {
    public FieldBlankExceptionHandler(String message) {
        super(message);
    }
}
