package com.example.taskservice.exception.user;

public class UserBadCredentialsException extends RuntimeException {
    public UserBadCredentialsException(String message) {
        super(message);
    }
}
