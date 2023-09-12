package com.kshrd.asset_tracer_api.exception;

public class ForbiddenExceptionHandler extends RuntimeException{
    public ForbiddenExceptionHandler(String message) {
        super(message);
    }
}
