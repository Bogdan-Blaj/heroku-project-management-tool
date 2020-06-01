package com.tool.ppmtool.exception.user;

public class UsernameAlreadyExistsExceptionResponse {
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UsernameAlreadyExistsExceptionResponse(String username) {
		this.username = username;
	}
	
	
}
