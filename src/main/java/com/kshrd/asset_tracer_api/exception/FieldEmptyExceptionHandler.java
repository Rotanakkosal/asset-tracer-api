package com.kshrd.asset_tracer_api.exception;

public class FieldEmptyExceptionHandler extends RuntimeException {
    public FieldEmptyExceptionHandler(String message) {
        super(message);
    }
}
