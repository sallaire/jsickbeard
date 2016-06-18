package org.sallaire.dto.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.sallaire.dao.db.entity.User;
import org.sallaire.dao.db.entity.User.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDto implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String password;
	private List<Role> roles;

	public UserDto() {

	}

	public UserDto(User user) {
		this(user, false);
	}

	public UserDto(User user, boolean setPassword) {
		this.id = user.getId();
		this.name = user.getName();
		if (setPassword) {
			this.password = user.getPassword();
		}
		this.roles = new ArrayList<>(user.getRoles());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
