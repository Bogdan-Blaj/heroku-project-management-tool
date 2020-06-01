package com.tool.ppmtool.exception.backlog;

public class BacklogIdentifierExceptionResponse {
	private String projectNotFound;

	public String getprojectNotFound() {
		return projectNotFound;
	}

	public void setprojectNotFound(String projectNotFound) {
		this.projectNotFound = projectNotFound;
	}

	public BacklogIdentifierExceptionResponse(String projectNotFound) {
		this.projectNotFound = projectNotFound;
	}
	
}
