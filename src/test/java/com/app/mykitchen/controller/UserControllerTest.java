package com.app.mykitchen.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.Role;
import com.app.mykitchen.domain.security.UserRole;
import com.app.mykitchen.service.RoleService;
import com.app.mykitchen.service.UserService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.app.mykitchen.controller.UserController")
public class UserControllerTest {

	private static final String USERNAME = "user1";
	private static final String EMAIL = "user1@gmail.com";
	private static final String PASSWORD = "password";
	private static final String ADMIN_ROLE = "Admin";

	private UserController userController;
	private User mockUser;

	@Before
	public void setUp() {
		userController = new UserController();
		userController.roleService = Mockito.mock(RoleService.class);
		userController.userService = Mockito.mock(UserService.class);
		mockUser = Mockito.mock(User.class);
		doReturn(getRoles()).when(userController.roleService).findAllRoles();
	}

	@Test
	public void testCreateUser() throws Exception {
		whenNew(User.class).withNoArguments().thenReturn(mockUser);
		doReturn(new User()).when(userController.userService).createUser(mockUser);

		userController.createUser(USERNAME, EMAIL, PASSWORD, Arrays.asList(ADMIN_ROLE));

		verify(mockUser, times(1)).setUsername(USERNAME);
		verify(mockUser, times(1)).setEmail(EMAIL);
		verify(mockUser, times(1)).setPassword(Mockito.anyString());
		verify(mockUser, times(1)).setUserRoles(Mockito.anySet());
	}

	@Test
	public void testUpdateUser() throws BusinessException {
		userController.updateUser(mockUser);

		verify(userController.userService, times(1)).updateUser(mockUser);
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
		return Arrays.asList(new Role(ADMIN_ROLE));
	}

}
