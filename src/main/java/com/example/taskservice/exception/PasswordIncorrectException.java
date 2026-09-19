package com.example.taskservice.exception;

public class PasswordIncorrectException extends RuntimeException {

    public PasswordIncorrectException(String message) {
        super("Password is incorrect" + (message.isBlank() ? "" : ": " + message));

    }
}
