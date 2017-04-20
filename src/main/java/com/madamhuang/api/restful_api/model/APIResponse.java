package com.madamhuang.api.restful_api.model;

public class APIResponse {

	private String code;
	private String message;
	private String token;
	private Object body;
	public APIResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public APIResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public APIResponse(String code, String message, String token, Object body) {
		super();
		this.code = code;
		this.message = message;
		this.token = token;
		this.body = body;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
}
