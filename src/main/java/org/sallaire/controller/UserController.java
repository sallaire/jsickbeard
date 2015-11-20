package org.sallaire.controller;

import org.sallaire.dto.user.Account;
import org.sallaire.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public void saveUser(@PathVariable("id") String id, @RequestParam("password") String password, @RequestParam("role") String role) {
		userService.saveUser(id, password, role);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable("id") String id) {
		userService.deleteAccount(id);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public Account getUser(@PathVariable("id") String id) {
		return userService.getUser(id);
	}
}
