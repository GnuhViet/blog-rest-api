package com.example.app.exception.category;

import lombok.Getter;

public class CategoryException extends RuntimeException{
    @Getter
    private String message;

    public CategoryException(String message) {
        this.message = message;
    }
}
