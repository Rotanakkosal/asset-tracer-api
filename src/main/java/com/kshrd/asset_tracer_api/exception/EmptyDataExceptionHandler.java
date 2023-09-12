package com.kshrd.asset_tracer_api.exception;

public class EmptyDataExceptionHandler extends RuntimeException {
    public EmptyDataExceptionHandler(String message) {
        super(message);
    }
}
