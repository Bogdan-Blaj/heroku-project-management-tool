package com.tool.ppmtool.exception.backlog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BacklogIdentifierException extends RuntimeException{

	private static final long serialVersionUID = -2901065275605279903L;

	public BacklogIdentifierException(String message) {
		super(message);
	}

	
}
