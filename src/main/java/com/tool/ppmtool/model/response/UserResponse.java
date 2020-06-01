package com.tool.ppmtool.model.response;

import java.util.Date;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

public class UserResponse {
	 private Long id;
	 private String username;
    
	 private String fullName;
    
    
	 private Date create_At;
	 private Date update_At;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Date getCreate_At() {
		return create_At;
	}
	public void setCreate_At(Date create_At) {
		this.create_At = create_At;
	}
	public Date getUpdate_At() {
		return update_At;
	}
	public void setUpdate_At(Date update_At) {
		this.update_At = update_At;
	}
	 
	 
	 
}
