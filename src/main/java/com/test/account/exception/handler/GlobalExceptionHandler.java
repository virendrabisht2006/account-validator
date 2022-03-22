package com.test.account.exception.handler;

import com.test.account.exception.AccountValidationException;
import com.test.account.exception.InvalidRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<Object> handleAccountValidation(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidRequest.class)
    public ResponseEntity<Object> handleAccountValidationRequest(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
