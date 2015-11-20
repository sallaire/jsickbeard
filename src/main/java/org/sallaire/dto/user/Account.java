package org.sallaire.dto.user;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Account implements Serializable {
	private static final long serialVersionUID = -3410650030695083213L;

	public static enum Role {
		SYSADMIN, ADMIN, USER;

		public String getRoleName() {
			return "ROLE_" + this.name();
		}
	}

	private String user;
	private String password;
	private List<Role> roles;

	public Account() {
		super();
	}

	public Account(String user, String password, Role... roles) {
		super();
		this.user = user;
		this.password = password;
		this.roles = Arrays.asList(roles);
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
