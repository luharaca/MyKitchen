package com.app.mykitchen.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.app.mykitchen.service.RoleService;
import com.app.mykitchen.service.UserService;

public class UserControllerTest {

	private static final String USERNAME = "user1";
	private static final String EMAIL = "user1@gmail.com";
	private static final String PASSWORD = "password";

	private UserController userController;

	@Mock
	RoleService roleService;
	@Mock
	UserService userService;

	@Before
	public void setUp() {
		userController = new UserController();
		userController.roleService = roleService;
		userController.userService = userService;
	}

	@Test
	public void test() {

		// userController.createUser(USERNAME, EMAIL, PASSWORD, Arrays.asList("Admin"));
	}

}
