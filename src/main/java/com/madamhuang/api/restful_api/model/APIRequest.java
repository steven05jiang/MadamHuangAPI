package com.madamhuang.api.restful_api.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class APIRequest {

    private String apiKey = "";

    private String token = "";

    private String operator = "";

    private JsonObject body  = null;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public JsonObject getBody() {
        return body;
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }

    public static APIRequest parse(String json) {
        return (APIRequest) parse(json, APIRequest.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object parse(String json, Class cls) {
        Gson gson = new Gson();
        Object object = gson.fromJson(json, cls);
        return object;
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object parseBody(Class cls) {
        Gson gson = new Gson();
        Object object = gson.fromJson(body, cls);
        return object;
    }
}

