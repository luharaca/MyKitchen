package com.app.mykitchen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.common.MailConstructor;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.PasswordResetToken;
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
	private static final String TOKEN = "token";

	@Mock
	Model mockModel;
	@Mock
	UserService userService;
	@Mock
	JavaMailSender mailSender;
	@Mock
	MailConstructor mailConstructor;

	@Before
	public void setUp() {
		homeController.userController = Mockito.mock(UserController.class);
		homeController.mailSender = mailSender;
		homeController.mailConstructor = mailConstructor;
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
	public void testLoginWithTokenSucess() {
		doReturn(getPasswordResetToken()).when(homeController.userController).getPasswordResetToken(TOKEN);

		String returnPage = homeController.login(TOKEN, mockModel);

		verify(mockModel, times(1)).addAttribute("editActive", true);
		assertEquals("myProfile", returnPage);
	}

	@Test
	public void testForgetPasswordSuccess() {
		String returnPage = homeController.forgetPassword(mockModel);

		verify(mockModel, times(1)).addAttribute("forgetPasswordActive", true);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testForgetPasswordPostSuccess() throws BusinessException {
		doReturn(buildValidUser()).when(homeController.userController).findUserByEmail(VALID_EMAIL);
		doReturn(new SimpleMailMessage()).when(homeController.mailConstructor).buildEmail(Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any());

		String returnPage = homeController.forgetPasswordPost(null, VALID_EMAIL, mockModel);

		verify(homeController.mailSender, times(1)).send(Mockito.any(SimpleMailMessage.class));
		verify(mockModel, times(1)).addAttribute("newPasswordSent", true);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testForgetPasswordPostFail() throws BusinessException {
		doReturn(null).when(homeController.userController).findUserByEmail(VALID_EMAIL);

		String returnPage = homeController.forgetPasswordPost(null, VALID_EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("emailNotExist", true);
		verify(mockModel, times(1)).addAttribute("email", VALID_EMAIL);
		assertEquals("myAccount", returnPage);
	}

	@Test
	public void testLoginWithInvalidTokenWithoutUserFail() {
		PasswordResetToken passwordToken = getPasswordResetToken();
		passwordToken.setUser(null);
		doReturn(passwordToken).when(homeController.userController).getPasswordResetToken(TOKEN);

		String returnPage = homeController.login(TOKEN, mockModel);

		verify(mockModel, times(1)).addAttribute("message", "The token is invalid");
		assertEquals("redirect:/badRequest", returnPage);
	}

	@Test
	public void testLoginWithExpiredTokenFail() {
		PasswordResetToken passwordToken = getPasswordResetToken();
		passwordToken.setExpiryDate(
				Date.from(LocalDate.of(1000, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		doReturn(passwordToken).when(homeController.userController).getPasswordResetToken(TOKEN);

		String returnPage = homeController.login(TOKEN, mockModel);

		verify(mockModel, times(1)).addAttribute("message", "The token is invalid");
		assertEquals("redirect:/badRequest", returnPage);
	}

	@Test
	public void testLoginWithNullTokenFail() {
		doReturn(null).when(homeController.userController).getPasswordResetToken(TOKEN);

		String returnPage = homeController.login(TOKEN, mockModel);

		verify(mockModel, times(1)).addAttribute("message", "The token is invalid");
		assertEquals("redirect:/badRequest", returnPage);
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

	private PasswordResetToken getPasswordResetToken() {
		PasswordResetToken token = new PasswordResetToken();
		token.setExpiryDate(
				Date.from(LocalDate.of(3000, 1, 1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		token.setUser(buildValidUser());
		return token;
	}

}
