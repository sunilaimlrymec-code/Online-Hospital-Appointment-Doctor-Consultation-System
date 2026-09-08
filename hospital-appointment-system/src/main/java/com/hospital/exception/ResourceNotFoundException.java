package com.hospital.exception;

/** Thrown when an entity looked up by id/email does not exist. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
