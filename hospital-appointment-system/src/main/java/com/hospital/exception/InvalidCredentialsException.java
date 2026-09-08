package com.hospital.exception;

/** Thrown when login credentials fail validation. */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
