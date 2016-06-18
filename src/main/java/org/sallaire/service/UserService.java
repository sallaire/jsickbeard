package org.sallaire.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.sallaire.dao.db.UserRepository;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dao.db.entity.User.Role;
import org.sallaire.dto.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional
public class UserService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByName(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDto(user, true);
	}

	public void saveUser(String userName, String password, String role, UserDto currentUser) {
		if (!currentUser.getRoles().contains(Role.ADMIN) && !currentUser.getRoles().contains(Role.SYSADMIN) && !currentUser.getName().equals(userName)) {
			LOGGER.warn("Current user has not enough credentials to modify users");
			return;
		}
		List<Role> roles = null;
		Role convertedRole = null;
		User user = userRepository.findUserByName(userName);
		if (!(currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.SYSADMIN))) {
			if (user == null) {
				LOGGER.warn("Current user has no enough credentials to create users role");
				return;
			} else if (!user.getName().equals(currentUser.getName())) {
				LOGGER.warn("Current user has no enough credentials to create users role");
				return;
			}
		}
		if (role != null) {
			try {
				convertedRole = Role.valueOf(role);
				convertedRole = checkRole(convertedRole, currentUser.getRoles());
			} catch (IllegalArgumentException e) {
				LOGGER.warn("Input role {} is not a valide one, should be one of {}", role, Role.values());
			}

		}
		if (convertedRole == null) {
			if (user == null || CollectionUtils.isEmpty(user.getRoles())) {
				LOGGER.info("No role specified for user {}, it will be set to {} by default", userName, Role.USER);
				convertedRole = Role.USER;
			} else {
				roles = user.getRoles();
				LOGGER.debug("User already exists in db, conserve its roles : {}", roles);
			}
		}
		if (roles == null) {
			switch (convertedRole) {
			case USER:
				roles = Lists.newArrayList(Role.USER);
				break;
			case ADMIN:
				if (currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.SYSADMIN)) {
					roles = Lists.newArrayList(Role.USER, Role.ADMIN);
				} else {
					LOGGER.warn("Current user has no enough credentials to modify users role");
					roles = Lists.newArrayList(Role.USER);
				}
				break;
			case SYSADMIN:
				roles = Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN);
				break;
			}
		}
		saveUser(userName, password, roles);
	}

	private Role checkRole(Role wantedRole, List<Role> currentRoles) {
		Role computedRole = null;
		switch (wantedRole) {
		case USER:
			computedRole = Role.USER;
			break;
		case ADMIN:
			if (currentRoles.contains(Role.ADMIN) || currentRoles.contains(Role.SYSADMIN)) {
				computedRole = Role.ADMIN;
			} else {
				LOGGER.warn("Current user has no enough credentials to modify user role to {}", Role.ADMIN);
				computedRole = null;
			}
			break;
		case SYSADMIN:
			if (currentRoles.contains(Role.SYSADMIN)) {
				computedRole = Role.SYSADMIN;
			} else {
				LOGGER.warn("Current user has no enough credentials to modify user role to {}", Role.SYSADMIN);
				computedRole = null;
			}
			break;
		}
		return computedRole;
	}

	private void saveUser(String userName, String password, List<Role> roles) {
		LOGGER.info("Saving user {} with password {} and role {}", userName, StringUtils.repeat("X", password.length()), roles);

		User user = userRepository.findUserByName(userName);
		if (user == null) {
			user = new User(userName, passwordEncoder.encode(password));
		} else if (password != null) {
			user.setPassword(passwordEncoder.encode(password));
		}
		user.setRoles(roles);
		userRepository.save(user);
		LOGGER.info("User saved");
	}

	public UserDto getUser(String userName, UserDto currentUser) {
		if (!currentUser.getName().equals(userName) && (currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.SYSADMIN))) {
			LOGGER.warn("Current user has no enough credentials to get user details");
			return null;
		}
		User user = userRepository.findUserByName(userName);
		if (user != null) {
			return new UserDto(user);
		}
		return null;
	}

	public boolean hasUsers() {
		return userRepository.count() > 0;
	}

	public void deleteUser(String name, UserDto currentUser) {
		if (!currentUser.getName().equals(name) && currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.SYSADMIN)) {
			LOGGER.warn("Current user has no enough credentials to delete user");
			return;
		}
		User user = userRepository.findUserByName(name);
		if (user != null) {
			userRepository.delete(user);
			LOGGER.debug("User {} has been deleted", name);
		} else {
			LOGGER.warn("Unable to delete user {}, it doesn't exist in database", name);
		}
	}

	public boolean authenticateAccount(String userName, String password) {
		User user = userRepository.findUserByName(userName);
		if (user != null) {
			return user.getPassword().equals(passwordEncoder.encode(password));
		} else {
			return false;
		}
	}

	@PostConstruct
	public void postConstruct() {
		LOGGER.info("Check existing user");
		if (!hasUsers()) {
			LOGGER.info("No user in db, creating default one");
			saveUser("admin", "admin", Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN));
		}
	}

}
