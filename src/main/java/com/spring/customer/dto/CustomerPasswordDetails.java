package com.spring.customer.dto;

import javax.validation.constraints.NotBlank;

public class CustomerPasswordDetails {

	@NotBlank(message = "Old password cannot be empty")
	private String oldPassword;
	@NotBlank(message = "*New password cannot be empty")
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	
}
