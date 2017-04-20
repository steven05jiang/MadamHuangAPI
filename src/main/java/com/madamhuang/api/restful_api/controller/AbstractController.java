package com.madamhuang.api.restful_api.controller;

public abstract class AbstractController {

    protected boolean validateAPIKey(String apiKey) {
        return true;
    }



}
