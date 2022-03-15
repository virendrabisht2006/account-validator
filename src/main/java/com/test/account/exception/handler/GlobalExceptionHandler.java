package com.test.account.exception.handler;

import com.test.account.exception.AccountValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<Object> handleAccountValidation(Exception e){

       return null;
    }
}
