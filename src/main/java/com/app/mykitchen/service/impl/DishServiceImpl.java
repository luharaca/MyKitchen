package com.app.mykitchen.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mykitchen.common.InternalServerException;
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

	@Override
	public List<Dish> findAllDishes() {
		return (List<Dish>) dishRepository.findAll();
	}

	@Override
	public Dish findDishById(Long id) throws InternalServerException {
		if (id != null) {
			Optional<Dish> dish = dishRepository.findById(id);
			if (dish.isPresent()) {
				return dish.get();
			}
			return null;
		}

		throw new InternalServerException("Dish id was null");
	}

}
