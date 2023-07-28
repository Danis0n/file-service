package com.example.fileservice.exception.handler;

public class CustomFileAlreadyExistsException extends RuntimeException {
    public CustomFileAlreadyExistsException(String message) {
        super(message);
    }
}
