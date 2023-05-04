package com.example.security.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;

public class RegisterException extends AuthenticationException {
    private BindingResult bindingResult;

    public RegisterException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
