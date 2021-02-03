package com.app.mykitchen.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.app.mykitchen.common.InternalServerException;
import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.service.DishService;

@RunWith(MockitoJUnitRunner.class)
public class AdminDishControllerTest {
	private AdminDishController adminDishController = new AdminDishController();
	private List<Dish> menu;

	@Mock
	Model mockModel;
	@Mock
	Dish dish;
	@Mock
	MultipartFile dishImage;

	@Before
	public void setup() throws InternalServerException {
		adminDishController.dishService = Mockito.mock(DishService.class);
		menu = new ArrayList<>();
	}

	@Test
	public void testAddDishPostSuccess() throws IOException {
		doReturn(dish).when(adminDishController.dishService).createDish(dish);
		doReturn(dishImage).when(dish).getDishImage();
		doReturn(new byte[0]).when(dishImage).getBytes();

		String returnPage = adminDishController.addDishPost(dish);

		assertEquals("redirect:menu", returnPage);
	}

	@Test
	public void testAddDishSuccess() {
		String returnPage = adminDishController.addDish(mockModel);

		verify(mockModel, times(1)).addAttribute(anyString(), any(Dish.class));
		assertEquals("addDish", returnPage);
	}

	@Test
	public void testViewMenuSuccess() {
		doReturn(menu).when(adminDishController.dishService).findAllDishes();

		String returnPage = adminDishController.viewMenu(mockModel);

		verify(mockModel, times(1)).addAttribute("menu", menu);
		assertEquals("menu", returnPage);
	}
}
