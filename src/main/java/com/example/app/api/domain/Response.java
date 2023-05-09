package com.example.app.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class Response<T> {
    private List<T> data;
    private boolean succeeded;
    private String[] errors;
    private String message;
}
