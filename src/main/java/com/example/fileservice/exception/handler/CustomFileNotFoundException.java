package com.example.fileservice.exception.handler;

public class CustomFileNotFoundException extends RuntimeException {
    public CustomFileNotFoundException(String message) {
        super(message);
    }
}
