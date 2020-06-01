package com.tool.ppmtool.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = -1873448917706771993L;

	public UsernameAlreadyExistsException(String message) {
		super(message);
	}

}
