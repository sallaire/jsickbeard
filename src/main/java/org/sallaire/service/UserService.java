package org.sallaire.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.sallaire.dao.db.TvShowRepository;
import org.sallaire.dao.db.UserRepository;
import org.sallaire.dao.db.entity.TvShow;
import org.sallaire.dao.db.entity.User;
import org.sallaire.dao.db.entity.User.Role;
import org.sallaire.dao.metadata.IMetaDataDao;
import org.sallaire.dto.api.AccountDto;
import org.sallaire.dto.exception.UnauthorizedException;
import org.sallaire.dto.user.ModifiedUserDto;
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
	private IMetaDataDao metaDataDao;
	
	@Autowired
	private TvShowRepository tvShowRepository;

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

	public void saveUser(ModifiedUserDto modifiedUser, UserDto currentUser) {
		//		if (StringUtils.isNotBlank(modifiedUser.getUsername())) {
		//			if (userRepository.findUserByName(modifiedUser.getUsername()) != null) {
		//				LOGGER.warn("User name already exists");
		//				return;
		//			}
		//		}
		if (passwordEncoder.matches(modifiedUser.getCurrentPassword(), currentUser.getPassword())) {
			saveUser(currentUser.getId(), null, modifiedUser.getNewPassword());
		} else {
			throw new UnauthorizedException();
		}
	}

	public void saveUser(long userId, UserDto newUser, UserDto currentUser) {
		User userToUpdate = userRepository.findOne(userId);
		if (userToUpdate == null) {
			LOGGER.warn("Unable to find user with id {} in database", userId);
			return;
		}

		if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.SYSADMIN) {
			LOGGER.warn("Current user has not enough credentials to modify users");
			return;
		}
		if ((userToUpdate.getRoles().contains(Role.ADMIN) || userToUpdate.getRoles().contains(Role.SYSADMIN)) && currentUser.getRole() != Role.SYSADMIN) {
			LOGGER.warn("Current user must be {} to modify user", Role.SYSADMIN);
			return;
		}


		switch (newUser.getRole()) {
		case USER:
			userToUpdate.setRoles(Lists.newArrayList(Role.USER));
			break;
		case ADMIN:
			if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SYSADMIN) {
				userToUpdate.setRoles(Lists.newArrayList(Role.USER, Role.ADMIN));
			} else {
				LOGGER.warn("Current user has no enough credentials to modify users role");
			}
			break;
		case SYSADMIN:
			if (currentUser.getRole() == Role.SYSADMIN) {
				userToUpdate.setRoles(Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN));
			} else {
				LOGGER.warn("Current user has no enough credentials to modify users role");
			}
			break;
		}
		if (StringUtils.isNotBlank(newUser.getPassword())) {
			userToUpdate.setPassword(passwordEncoder.encode(newUser.getPassword()));
		}
		if (StringUtils.isNotBlank(newUser.getName())) {
			userToUpdate.setName(newUser.getName());
		}
		userRepository.save(userToUpdate);
	}


	private void saveUser(Long userId, String userName, String password) {
		LOGGER.info("Saving user {} with password {}", userName, StringUtils.repeat("X", password.length()));

		User user = userRepository.findOne(userId);
		if (StringUtils.isNotBlank(password)) {
			user.setPassword(passwordEncoder.encode(password));
		}
		if (StringUtils.isNotBlank(userName)) {
			user.setName(userName);
		}
		userRepository.save(user);
		LOGGER.info("User saved");
	}

	public void addUser(UserDto newUser, UserDto currentUser) {
		if (currentUser.getRole() == Role.USER) {
			LOGGER.warn("Current user has no enough credentials to create user");
			return;
		}

		List<Role> roles = null;
		switch (newUser.getRole()) {
		case USER:
			roles = Lists.newArrayList(Role.USER);
			break;
		case ADMIN:
			roles = Lists.newArrayList(Role.USER, Role.ADMIN);
			break;
		case SYSADMIN:
			if (currentUser.getRole() == Role.SYSADMIN) {
				roles = Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN);
			} else {
				LOGGER.warn("Current user has no enough credentials to modify users role");
			}
			break;
		}

		if (roles != null) {
			addUser(newUser.getUsername(), newUser.getPassword(), roles);
		}

	}

	private void addUser(String userName, String password, List<Role> roles) {
		LOGGER.info("Saving user {} with password {} and role {}", userName, StringUtils.repeat("X", password.length()), roles);

		User user = new User();
		user.setName(userName);
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(roles);
		userRepository.save(user);
		LOGGER.info("User saved");
	}

	public UserDto getUser(Long userId, UserDto currentUser) {
		if (!currentUser.getId().equals(userId) && (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SYSADMIN)) {
			LOGGER.warn("Current user has no enough credentials to get user details");
			return null;
		}
		User user = userRepository.findOne(userId);
		if (user != null) {
			return new UserDto(user);
		}
		return null;
	}

	public boolean hasUsers() {
		return userRepository.count() > 0;
	}

	public void deleteUser(Long userId, UserDto currentUser) {
		if (!currentUser.getId().equals(userId) && (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SYSADMIN)) {
			LOGGER.warn("Current user has no enough credentials to delete user");
			return;
		}
		User user = userRepository.findOne(userId);
		if (user != null) {
			userRepository.delete(user);
			LOGGER.debug("User {} has been deleted", user.getName());
		} else {
			LOGGER.warn("Unable to delete user {}, it doesn't exist in database", userId);
		}
	}

	public boolean authenticateAccount(String userName, String password) {
		User user = userRepository.findUserByName(userName);
		if (user != null) {
			return passwordEncoder.matches(password, user.getPassword());
		} else {
			return false;
		}
	}

	@PostConstruct
	public void postConstruct() {
		LOGGER.info("Check existing user");
		if (!hasUsers()) {
			LOGGER.info("No user in db, creating default one");
			addUser("admin", "admin", Lists.newArrayList(Role.USER, Role.ADMIN, Role.SYSADMIN));
		}
	}

	public AccountDto getAccount(UserDto currentUser) {
		AccountDto account = new AccountDto();
		User user = userRepository.findOne(currentUser.getId());
		account.setUsername(currentUser.getName());
		account.setInscriptionDate(user.getInscriptionDate() != null ? user.getInscriptionDate() : LocalDate.now());

		List<TvShow> shows = new ArrayList<TvShow>(tvShowRepository.findByConfigurationsFollowersId(user.getId()));
		account.setNbShows(shows.size());
		account.setShowsByGenre(shows.stream()
				.map(TvShow::getGenres)
				.flatMap(Collection::stream)
				.collect(
						Collectors.groupingBy(Function.identity(), Collectors.counting())
						));

		account.setShowsByNetwork(shows.stream()
				.map(TvShow::getNetwork)
				.flatMap(Collection::stream)
				.collect(
						Collectors.groupingBy(Function.identity(), Collectors.counting())
						));
		account.setBannerUrl(metaDataDao.getImageUrl(shows.get((int) (Math.random() * shows.size())).getBanner()));
		return account;
	}

}
