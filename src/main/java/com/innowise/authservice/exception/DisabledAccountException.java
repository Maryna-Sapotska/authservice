package com.innowise.authservice.exception;

public class DisabledAccountException extends RuntimeException {

    public DisabledAccountException(String message) {
        super(message);
    }
}
