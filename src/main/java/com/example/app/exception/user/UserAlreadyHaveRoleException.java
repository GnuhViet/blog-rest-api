package com.example.app.exception.user;

import lombok.Getter;

@Getter
public class UserAlreadyHaveRoleException extends RuntimeException{
    private final String message;

    public UserAlreadyHaveRoleException(String message) {
        this.message = message;
    }
}
