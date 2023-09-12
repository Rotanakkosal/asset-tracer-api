package com.kshrd.asset_tracer_api.exception;

public class IsCredentialsNonExpiredExceptionHandler extends RuntimeException {
    public IsCredentialsNonExpiredExceptionHandler(String message) {
        super(message);
    }
}
