package com.app.mykitchen.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.Role;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.UserRole;
import com.app.mykitchen.service.RoleService;
import com.app.mykitchen.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	private static final String USERNAME = "user1";
	private static final String EMAIL = "user1@gmail.com";
	private static final String PASSWORD = "password";
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String TOKEN = "ASDFASDFASDFASDFASDF";

	private UserController userController;

	@Mock
	User mockUser;
	@Mock
	Model mockModel;

	@Before
	public void setUp() {
		userController = new UserController();
		userController.roleService = Mockito.mock(RoleService.class);
		userController.userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testCreateUser() throws Exception {
		User user = buildValidUser();
		doReturn(getRoles()).when(userController.roleService).findAllRoles();
		doReturn(user).when(userController.userService).createUser(Mockito.any());

		User result = userController.createUser(USERNAME, EMAIL, PASSWORD, Arrays.asList(ADMIN_ROLE));

		assertEquals(user.getUsername(), result.getUsername());
	}

	@Test
	public void testUpdateUser() throws BusinessException {
		userController.updateUser(mockUser);

		verify(userController.userService, times(1)).updateUser(mockUser);
	}

	@Test
	public void testFindUserByEmail() throws BusinessException {
		userController.findUserByEmail(EMAIL);

		verify(userController.userService, times(1)).findUserByEmail(EMAIL);
	}

	@Test
	public void testFindUserByUsername() throws BusinessException {
		userController.findUserByUsername(USERNAME);

		verify(userController.userService, times(1)).findUserByUsername(USERNAME);
	}

	@Test
	public void testNoAdminRoleAssignedToUser() throws BusinessException {
		boolean result = userController.adminRoleAssignedToUser(mockUser);

		assertFalse(result);
	}

	@Test
	public void testAdminRoleAssignedToUser() throws BusinessException {
		boolean result = userController.adminRoleAssignedToUser(buildValidUser());

		assertTrue(result);
	}

	@Test
	public void testCreateUserToken() {
		userController.createUserToken(mockUser, TOKEN);

		verify(userController.userService, times(1)).createPasswordResetToken(Mockito.any());
	}

	@Test
	public void testUserExistByUsername() {
		User user = buildValidUser();
		doReturn(user).when(userController.userService).findUserByUsername(USERNAME);

		boolean exists = userController.userExists(USERNAME, EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("usernameExists", true);
		verify(mockModel, times(1)).addAttribute("username", user.getUsername());
		assertTrue(exists);
	}

	@Test
	public void testUserExistByEmail() {
		User user = buildValidUser();
		doReturn(user).when(userController.userService).findUserByEmail(EMAIL);

		boolean exists = userController.userExists(USERNAME, EMAIL, mockModel);

		verify(mockModel, times(1)).addAttribute("emailExists", true);
		verify(mockModel, times(1)).addAttribute("email", user.getEmail());
		assertTrue(exists);
	}

	@Test
	public void testUserNotExist() {
		boolean exists = userController.userExists(USERNAME, EMAIL, mockModel);

		assertFalse(exists);
	}

	@Test
	public void testGetPasswordResetToken() {
		userController.getPasswordResetToken(TOKEN);

		verify(userController.userService, times(1)).getPasswordResetToken(TOKEN);
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

	private List<Role> getRoles() {
		return Arrays.asList(new Role(ADMIN_ROLE), new Role("CUSTOMER"));
	}

}
