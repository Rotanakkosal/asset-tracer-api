package com.kshrd.asset_tracer_api.exception;

public class OutOfAmountExceptionHandler extends RuntimeException {
    public OutOfAmountExceptionHandler(String message) {
        super(message);
    }
}
