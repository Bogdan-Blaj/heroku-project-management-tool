package com.tool.ppmtool.exception.project;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException{

	private static final long serialVersionUID = 4322643674050436401L;
	
	public ProjectIdException(String message) {
		super(message);
	}
	
}
