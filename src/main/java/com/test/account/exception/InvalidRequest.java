package com.test.account.exception;

public class InvalidRequest extends RuntimeException {
    public InvalidRequest(String message){
        super(message);
    }

    public InvalidRequest(String message, Exception e){
        super(message, e);
    }
}
