package com.hospital.exception;

/** Thrown on registration when the email is already in use. */
public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
