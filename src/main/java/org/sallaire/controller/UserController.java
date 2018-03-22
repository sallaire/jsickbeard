package org.sallaire.controller;

import org.sallaire.controller.conf.CurrentUser;
import org.sallaire.dto.api.AccountDto;
import org.sallaire.dto.user.ModifiedUserDto;
import org.sallaire.dto.user.UserDto;
import org.sallaire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PutMapping(value = "/user/{id}")
	public void saveUser(@PathVariable("id") Long id, @RequestBody UserDto modifiedUser , @CurrentUser UserDto currentUser) {
		userService.saveUser(id, modifiedUser, currentUser);
	}

	@PostMapping(value = "/user")
	public void createUser(@RequestBody UserDto newUser , @CurrentUser UserDto currentUser) {
		userService.addUser(newUser, currentUser);
	}

	@PutMapping(value = "/user")
	public void saveUser(@RequestBody ModifiedUserDto modifiedUser , @CurrentUser UserDto currentUser) {
		userService.saveUser(modifiedUser, currentUser);
	}

	@DeleteMapping(value = "/user/{id}")
	public void deleteUser(@PathVariable("id") Long id, @CurrentUser UserDto currentUser) {
		userService.deleteUser(id, currentUser);
	}
	
	@DeleteMapping(value = "/user")
	public void deleteUser(@CurrentUser UserDto currentUser) {
		userService.deleteUser(currentUser.getId(), currentUser);
	}

	@GetMapping(value = "/user/{id} ")
	public UserDto getUser(@PathVariable("id") Long id, @CurrentUser UserDto currentUser) {
		return userService.getUser(id, currentUser);
	}

	@GetMapping(value = "/user")
	public UserDto getUser(@CurrentUser UserDto currentUser) {
		return currentUser;
	}
	
	@GetMapping(value = "/account")
	public AccountDto getAccount(@CurrentUser UserDto currentUser) {
		return userService.getAccount(currentUser);
	}

}
