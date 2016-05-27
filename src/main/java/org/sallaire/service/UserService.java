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

	public void saveUser(String userName, String password, String role) {
		LOGGER.info("Saving user {} with password {} and role {}", userName, StringUtils.repeat("X", password.length()), role);
		Role convertedRole = null;
		List<Role> roles = null;
		User user = userRepository.findUserByName(userName);
		if (user == null) {
			user = new User(userName, passwordEncoder.encode(password));
		} else {
			user.setPassword(passwordEncoder.encode(password));
		}
		if (role == null) {
			if (CollectionUtils.isEmpty(user.getRoles())) {
				LOGGER.info("No role specified for user {}, it will be set to {} by default", userName, Role.USER);
				convertedRole = Role.USER;
			} else {
				roles = user.getRoles();
				LOGGER.debug("User already exists in db, conserve its roles : {}", roles);
			}
		} else {
			try {
				convertedRole = Role.valueOf(role);
			} catch (IllegalArgumentException e) {
				LOGGER.warn("Input role {} is not a valide one, should be one of {}, the user will be saved with default role {}", role, Role.values(), Role.USER);
				convertedRole = Role.USER;
			}
		}
		if (convertedRole != null) {
			if (roles == null) {
				switch (convertedRole) {
				case USER:

					roles = Lists.newArrayList(Role.USER);
					break;
				case ADMIN:
					roles = Lists.newArrayList(Role.USER, Role.ADMIN);
					break;
				case SYSADMIN:
					roles = Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN);
					break;
				}
				LOGGER.debug("Roles computed : {}", roles);
				user.setRoles(roles);
			}

			userRepository.save(user);
			LOGGER.info("User saved");
		}
	}

	public UserDto getUser(String userName) {
		User user = userRepository.findUserByName(userName);
		if (user != null) {
			return new UserDto(user);
		}
		return null;
	}

	public boolean hasUsers() {
		return userRepository.count() > 0;
	}

	public void deleteUser(String name) {
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
			saveUser("admin", "admin", Role.SYSADMIN.name());
		}
	}

}
