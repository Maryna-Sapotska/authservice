package com.innowise.authservice.exception;

public class UserCredentialsException extends RuntimeException {
    public UserCredentialsException() {

    }
    public UserCredentialsException(String message) {
        super(message);
    }

    public UserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public  UserCredentialsException(Throwable cause) {
        super(cause);
    }
}
