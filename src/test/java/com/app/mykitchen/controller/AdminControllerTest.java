package com.app.mykitchen.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.util.SecurityUtils;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

	private final static String VALID_USERNAME = "Mike";
	private static User validUser;

	@Spy
	private AdminController adminController;

	@Before
	public void setup() {
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

	@Test
	public void testAdminLoginSuccess() {
		Model mockModel = Mockito.mock(Model.class);

		String returnPageName = adminController.adminLogin(mockModel);

		Assert.assertEquals("adminLogin", returnPageName);
	}

	@Test
	public void testAdminHomeSuccess() {
		Model mockModel = Mockito.mock(Model.class);

		String returnPageName = adminController.adminHome(mockModel);

		Assert.assertEquals("adminHome", returnPageName);
	}

	@Test
	public void testAdminHomePostSuccess() {
		Model mockModel = Mockito.mock(Model.class);
		Mockito.doReturn(validUser).when(adminController.userController).findUserByUsername(validUser.getUsername());
		Mockito.doReturn(true).when(adminController.userController).adminRoleAssignedToUser(Mockito.any());
		Mockito.doReturn(true).when(adminController).passwordEncoded(validUser, "123");

		String returnPageName = adminController.adminLoginPost(VALID_USERNAME, "123", mockModel);

		Assert.assertEquals("adminHome", returnPageName);
	}

	private static User buildValidUser() {
		User user = new User();
		user.setUsername(VALID_USERNAME);
		user.setPassword(SecurityUtils.passwordEncoder().encode("123"));
		return user;
	}
}
