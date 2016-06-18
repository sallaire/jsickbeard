package org.sallaire.dao.db.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class User {

	private static final long serialVersionUID = 9178995861257235991L;

	public static enum Role {
		SYSADMIN, ADMIN, USER;

		public String getRoleName() {
			return "ROLE_" + this.name();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String password;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<Role> roles;
	@ManyToMany
	private Set<TvShowConfiguration> configurations;

	public User() {
		super();
	}

	public User(String name, String password, Role... roles) {
		super();
		this.name = name;
		this.password = password;
		this.roles = Arrays.asList(roles);
		this.configurations = new HashSet<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<TvShowConfiguration> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Set<TvShowConfiguration> configurations) {
		this.configurations = configurations;
	}

	public void addConfiguration(TvShowConfiguration configuration) {
		this.configurations.add(configuration);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
