package com.example.taskservice.exception.user;

public class EmailNotUniqueException extends RuntimeException {
    public EmailNotUniqueException(String message) {
        super(message);
    }
}
