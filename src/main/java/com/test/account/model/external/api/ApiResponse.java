package com.test.account.model.external.api;

public class ApiResponse {
    private boolean isValid;

    public ApiResponse(){
        //default constructor
    }
    public ApiResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
