package org.sallaire.dto.admin;

import java.util.List;

import org.sallaire.dto.user.Quality;

public class AdminConfig {
	private Long id;
	private Quality quality;
	private String lang;
	private List<String> users;

	public AdminConfig(Long id, Quality quality, String lang) {
		super();
		this.id = id;
		this.quality = quality;
		this.lang = lang;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quality getQuality() {
		return quality;
	}

	public void setQuality(Quality quality) {
		this.quality = quality;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

}
