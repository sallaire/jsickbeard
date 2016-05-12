package org.sallaire.dao.db.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NodeEntity
public class User implements UserDetails {

	private static final long serialVersionUID = 9178995861257235991L;

	public static enum Role {
		SYSADMIN, ADMIN, USER;

		public String getRoleName() {
			return "ROLE_" + this.name();
		}
	}

	@GraphId
	private Long id;
	private String name;
	private String password;
	private List<Role> roles;

	@Relationship(type = "FOLLOWED_BY", direction = Relationship.UNDIRECTED)
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
		configuration.getFollowers().add(this);
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (roles != null) {
			return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
		} else {
			return null;
		}
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
