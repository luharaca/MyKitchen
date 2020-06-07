package com.app.mykitchen.service;

import java.util.List;

import com.app.mykitchen.common.InternalServerException;
import com.app.mykitchen.domain.Dish;

public interface DishService {
	
	Dish createDish(Dish dish);
	
	List<Dish> findAllDishes();
	
	Dish findDishById(Long id) throws InternalServerException;
}
