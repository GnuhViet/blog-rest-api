package com.example.app.exception;

import lombok.Getter;

public class NotFoundException extends RuntimeException{
    @Getter
    private String message;

    public NotFoundException(String message) {
        this.message = message;
    }
}
