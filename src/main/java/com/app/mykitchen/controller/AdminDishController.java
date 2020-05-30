package com.app.mykitchen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.mykitchen.domain.Dish;

@Controller
@RequestMapping("/admin/dish")
public class AdminDishController {

	@PostMapping("/add")
	public String addNewDish(Model model) {
		Dish dish = new Dish();
		model.addAttribute("dish", dish);
		return "addDish";
	}
	
	@GetMapping("/add")
	public String addDish(Model model) {
		Dish dish = new Dish();
		model.addAttribute("dish", dish);
		return "addDish";
	}
}
