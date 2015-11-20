package org.sallaire.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.sallaire.dao.db.UserDao;
import org.sallaire.dto.user.Account;
import org.sallaire.dto.user.Account.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = userDao.getAccount(username).orElseThrow(() -> new UsernameNotFoundException(username));
		return new User(username, account.getPassword(), account.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList()));
	}

	public void saveUser(String userName, String password, String role) {
		LOGGER.info("Saving user {} with password {} and role {}", userName, StringUtils.repeat("X", password.length()), role);
		Role convertedRole = null;
		Role[] roles = null;
		if (role == null) {
			Account existingAccount = userDao.getAccount(userName).orElse(new Account());
			if (CollectionUtils.isEmpty(existingAccount.getRoles())) {
				LOGGER.info("No role specified for user {}, it will be set to {} by default", userName, Role.USER);
				convertedRole = Role.USER;
			} else {
				roles = existingAccount.getRoles().stream().toArray(Role[]::new);
				LOGGER.debug("User already exists in db, conserve its roles : {}", (Object[]) roles);
			}
		} else {
			try {
				convertedRole = Role.valueOf(role);
			} catch (IllegalArgumentException e) {
				LOGGER.warn("Input role {} is not a valide one, should be one of {}, the user will not be saved", role, Role.values());
			}
		}
		if (convertedRole != null) {
			if (roles == null) {
				switch (convertedRole) {
				case USER:
					roles = new Role[] { Role.USER };
					break;
				case ADMIN:
					roles = new Role[] { Role.USER, Role.ADMIN };
					break;
				case SYSADMIN:
					roles = new Role[] { Role.USER, Role.ADMIN, Role.SYSADMIN };
					break;
				}
				LOGGER.debug("Roles computed : {}", (Object[]) roles);
			}
			userDao.saveAccount(new Account(userName, passwordEncoder.encode(password), roles));
			LOGGER.info("User saved");
		}
	}

	public Account getUser(String userName) {
		Account account = userDao.getAccount(userName).orElseGet(null);
		account.setPassword(null);
		return account;
	}

	public boolean hasAccounts() {
		Collection<Account> existingAccounts = userDao.getAccounts().orElse(new ArrayList<>());
		LOGGER.debug("{} accounts found in database", existingAccounts.size());
		return !existingAccounts.isEmpty();
	}

	public void deleteAccount(String name) {
		userDao.deleteUser(name);
	}

}
