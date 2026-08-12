package com.swapi.framework.core;

/**
 * Named HTTP status codes used across the suite, so tests read as intent
 * ({@code HttpStatus.FORBIDDEN.code()}) rather than magic numbers.
 */
public enum HttpStatus {

    OK(200),
    NOT_FOUND(404),
    FORBIDDEN(403),
    METHOD_NOT_ALLOWED(405);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
