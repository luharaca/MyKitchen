package com.app.mykitchen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.app.mykitchen.common.InternalServerException;
import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.service.DishService;
import com.app.mykitchen.service.UserService;

import ch.qos.logback.classic.Logger;

@RunWith(MockitoJUnitRunner.class)
public class DishControllerTest {
	private DishController dishController = new DishController();
	private Dish validDish = getDish();
	private User validUser = getUser();

	@Mock
	Model mockModel;

	@Mock
	Principal mockPrincipal;

	@Before
	public void setup() throws InternalServerException {
		dishController.dishService = Mockito.mock(DishService.class);
		dishController.userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testViewMenuSuccess() throws InternalServerException {

		String returnPage = dishController.viewMenu(mockModel);

		verify(dishController.dishService).findAllDishes();
		verify(mockModel).addAttribute(Mockito.anyString(), Mockito.anyList());
		assertEquals("menu", returnPage);
	}

	@Test
	public void testDishDetailsWithoutLoginSuccess() throws InternalServerException {
		Mockito.doReturn(validDish).when(dishController.dishService).findDishById(validDish.getId());

		String returnPage = dishController.dishDetails(validDish.getId(), null, mockModel);

		verify(dishController.dishService).findDishById(validDish.getId());
		verify(mockModel).addAttribute("dish", validDish);
		assertEquals("dishDetails", returnPage);
	}

	@Test
	public void testDishDetailsWithLoginSuccess() throws InternalServerException {
		Mockito.doReturn(validDish).when(dishController.dishService).findDishById(validDish.getId());
		Mockito.doReturn(validUser.getUsername()).when(mockPrincipal).getName();
		Mockito.doReturn(validUser).when(dishController.userService).findUserByUsername(validUser.getUsername());

		String returnPage = dishController.dishDetails(validDish.getId(), mockPrincipal, mockModel);

		verify(dishController.dishService).findDishById(validDish.getId());
		verify(mockModel).addAttribute("user", validUser);
		verify(mockModel).addAttribute("dish", validDish);
		assertEquals("dishDetails", returnPage);
	}

	@Test
	public void testDishDetailsFail() throws InternalServerException {
		dishController.logger = Mockito.mock(Logger.class);
		Mockito.doThrow(new InternalServerException("User is not found.")).when(dishController.dishService)
				.findDishById(validDish.getId());

		dishController.dishDetails(validDish.getId(), null, mockModel);

		verify(dishController.logger).error("User is not found.");

	}

	@Test
	public void testViewMenuFail() throws InternalServerException {

		String returnPage = dishController.viewMenu(null);

		verify(dishController.dishService).findAllDishes();
		verify(mockModel).addAttribute(Mockito.anyString(), Mockito.anyList());
		assertEquals("menu", returnPage);
	}

	private Dish getDish() {
		Dish dish = new Dish();
		dish.setId((long) 123);
		return dish;
	}

	private User getUser() {
		User user = new User();
		user.setUsername("User1");
		return user;
	}

}
