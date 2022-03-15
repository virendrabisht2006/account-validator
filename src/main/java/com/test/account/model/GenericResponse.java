package com.test.account.model;

import java.util.List;

public class GenericResponse {

    private List<ResponseResult> result;

    public GenericResponse(List<ResponseResult> result) {
        this.result = result;
    }

    public List<ResponseResult> getResult() {
        return result;
    }

    public void setResult(List<ResponseResult> result) {
        this.result = result;
    }
}
