package com.hospital.exception;

/** Thrown when a protected endpoint is called without a valid session. */
public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String message) {
        super(message);
    }
}
