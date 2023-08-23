package com.example.app.aspect.exceptionhandler;

import com.example.app.api.model.error.ApiError;
import com.example.app.exception.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(0)
@ControllerAdvice
public class CategoryExceptionHandler extends ResponseEntityExceptionHandler {

    // @ExceptionHandler(NotFoundException.class)
    // protected ResponseEntity<Object> handleCategoryException(NotFoundException ex) {
    //     ApiError apiError = new ApiError(HttpStatus.CONFLICT);
    //     apiError.setMessage(ex.getMessage());
    //     return RestExceptionHandler.buildResponseEntity(apiError);
    // }
}
