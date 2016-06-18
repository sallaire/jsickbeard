package org.sallaire.dto.admin;

public class AdminShow {
	private Long id;
	private String name;
	private int configurations;
	private int users;

	public AdminShow(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getConfigurations() {
		return configurations;
	}

	public void setConfigurations(int configurations) {
		this.configurations = configurations;
	}

	public int getUsers() {
		return users;
	}

	public void setUsers(int users) {
		this.users = users;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
