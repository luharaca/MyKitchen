package com.app.mykitchen.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.service.DishService;

@Controller
@RequestMapping("/admin/dish")
public class AdminDishController {
	private Logger logger = LoggerFactory.getILoggerFactory().getLogger("AdminDishController");

	@Autowired
	DishService dishService;

	@PostMapping("/add")
	public String addDishPost(@ModelAttribute("dish") Dish dish) {

		dishService.createDish(dish);

		MultipartFile dishImage = dish.getDishImage();
		
		String name = dish.getId() + ".png";

		try {
			byte[] bytes = dishImage.getBytes();
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File("src/main/resources/static/img/dish/" + name)));
			stream.write(bytes);
			stream.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} 
		
		return "redirect:menu";
	}

	@GetMapping("/add")
	public String addDish(Model model) {
		Dish dish = new Dish();
		model.addAttribute("dish", dish);
		return "addDish";
	}
	
	@GetMapping("/menu")
	public String viewMenu(Model model) {
		List<Dish> menu = dishService.findAllDishes();
	
		model.addAttribute("menu", menu);
	
		return "menu";
	}
}
