package com.app.mykitchen.controller;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.util.SecurityUtils;

public class AdminControllerTest {

	private final static String VALID_USERNAME = "Mike";
	private static User validUser;

	private static AdminController adminController = new AdminController();

	@BeforeClass
	public static void setup() {
		adminController.userController = Mockito.mock(UserController.class);
		validUser = buildValidUser();
	}

	@Test
	public void testAdminLoginPostFail() {
		Mockito.doReturn(validUser).when(adminController.userController).findUserByUsername(validUser.getUsername());
		Mockito.doReturn(true).when(adminController.userController).adminRoleAssignedToUser(validUser);
		Model model = Mockito.mock(Model.class);

		String returnPageName = adminController.adminLoginPost(VALID_USERNAME, "123", model);

		Assert.assertEquals("adminLogin", returnPageName);
	}

	private static User buildValidUser() {
		User user = new User();
		user.setUsername(VALID_USERNAME);
		user.setPassword(SecurityUtils.passwordEncoder().encode("123"));
		return user;
	}
}
