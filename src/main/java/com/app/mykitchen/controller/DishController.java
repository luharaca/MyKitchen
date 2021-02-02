package com.app.mykitchen.controller;

import java.security.Principal;
import java.util.List;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.mykitchen.common.InternalServerException;
import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.service.DishService;
import com.app.mykitchen.service.UserService;

@Controller
public class DishController {
	Logger logger = LoggerFactory.getLogger("DishController");

	@Autowired
	DishService dishService;
	@Autowired
	UserService userService;

	@GetMapping("/menu")
	public String viewMenu(Model model) {
		List<Dish> menu = dishService.findAllDishes();

		model.addAttribute("menu", menu);
		return "menu";
	}

	@GetMapping("/dishDetails")
	public String dishDetails(@PathParam("id") Long id, Principal principal, Model model) {

		if (principal != null) {
			User user = userService.findUserByUsername(principal.getName());
			model.addAttribute("user", user);
		}

		Dish dish = null;
		try {
			dish = dishService.findDishById(id);
		} catch (InternalServerException e) {
			logger.error(e.getMessage());
		}

		model.addAttribute("dish", dish);

		return "dishDetails";
	}

}
