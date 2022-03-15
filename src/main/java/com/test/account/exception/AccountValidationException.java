package com.test.account.exception;

public class AccountValidationException extends RuntimeException{
    public AccountValidationException(String message){
        super(message);
    }

    public AccountValidationException(String message, Exception e){
        super(message, e);
    }

}
