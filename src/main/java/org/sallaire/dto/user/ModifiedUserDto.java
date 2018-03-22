package org.sallaire.dto.user;

public class ModifiedUserDto {


	private String currentPassword;
	private String newPassword;

	public ModifiedUserDto() {

	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	
}
