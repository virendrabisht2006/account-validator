package com.test.account.model.external.provider;


public class ApiProvider {
    private String name;
    private String url;

    public ApiProvider() {
       //default constructor
    }

    public ApiProvider(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
