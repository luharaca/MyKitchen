package com.app.mykitchen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.repository.DishRepository;
import com.app.mykitchen.service.DishService;

@Service
public class DishServiceImpl implements DishService {
	
	@Autowired
	DishRepository dishRepository;

	@Override
	public Dish createDish(Dish dish) {
		return dishRepository.save(dish);
	}

}
