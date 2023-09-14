package com.example.app.aspect.exceptionhandler;

import com.example.app.api.model.error.ApiError;
import com.example.app.exception.EmptyRequestBodyException;
import com.example.app.exception.user.UserAlreadyHaveRoleException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(0)
@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserAlreadyHaveRoleException.class)
    protected ResponseEntity<Object> handleUserAlreadyHaveRoleException(UserAlreadyHaveRoleException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT);
        apiError.setMessage(ex.getMessage());
        return RestExceptionHandler.buildResponseEntity(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return RestExceptionHandler.buildResponseEntity(apiError);
    }
}
