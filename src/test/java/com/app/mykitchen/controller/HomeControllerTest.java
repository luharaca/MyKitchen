package com.app.mykitchen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.Role;
import com.app.mykitchen.domain.security.UserRole;
import com.app.mykitchen.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {
	private HomeController homeController = new HomeController();
	private static final String VALID_USERNAME = "user1";
	private static final String VALID_EMAIL = "user1@gmail.com";
	private static final String USERNAME = "user1";
	private static final String EMAIL = "user1@gmail.com";
	private static final String PASSWORD = "password";
	private static final String ADMIN_ROLE = "Admin";

	@Mock
	Model mockModel;
	@Mock
	UserService userService;

	@Before
	public void setUp() {
		homeController.userController = Mockito.mock(UserController.class);
	}

	@Test
	public void testIndexPageSuccess() {
		String returnPage = homeController.index();
		assertEquals("index", returnPage);
	}

	@Test
	public void testMyAccountSuccess() {
		String returnPage = homeController.myAccount();
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testCreateNewUserSuccess() {
		String returnPage = homeController.createNewUser(mockModel);

		verify(mockModel, times(1)).addAttribute("createUserActive", true);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testCreateExistUserPostSuccess() {
		doReturn(true).when(homeController.userController).userExists(VALID_USERNAME, VALID_EMAIL, mockModel);

		String returnPage = homeController.createNewUserPost(Mockito.mock(HttpServletRequest.class), VALID_USERNAME,
				VALID_EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("createUserActive", true);
		assertEquals("myAccount", returnPage);
	}

	@Ignore
	@Test
	public void testCreateNewUserPostSuccess() throws BusinessException {
		doReturn(false).when(homeController.userController).userExists(VALID_USERNAME, VALID_EMAIL, mockModel);
		// doReturn(buildValidUser()).when(homeController.userController).createUser(Mockito.anyString(),Mockito.anyString());

		String returnPage = homeController.createNewUserPost(Mockito.mock(HttpServletRequest.class), VALID_USERNAME,
				VALID_EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("emailNotExist", true);
		verify(mockModel, times(1)).addAttribute("email", VALID_EMAIL);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testLoginSuccess() {
		String returnPage = homeController.login(null, mockModel);

		verify(mockModel, times(1)).addAttribute("loginActive", true);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testForgetPasswordSuccess() {
		String returnPage = homeController.forgetPassword(mockModel);

		verify(mockModel, times(1)).addAttribute("forgetPasswordActive", true);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testForgetPasswordPost() throws BusinessException {
		doReturn(null).when(homeController.userController).findUserByEmail(VALID_EMAIL);

		String returnPage = homeController.forgetPasswordPost(null, VALID_EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("emailNotExist", true);
		verify(mockModel, times(1)).addAttribute("email", VALID_EMAIL);
		assertEquals("myAccount", returnPage);
	}

	private User buildValidUser() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);

		Role role = new Role();
		role.setName(ADMIN_ROLE);

		Set<UserRole> set = new HashSet<>();
		set.add(new UserRole(user, role));
		user.setUserRoles(set);

		return user;
	}

}
