package com.app.mykitchen.repository;

import org.springframework.data.repository.CrudRepository;

import com.app.mykitchen.domain.Dish;

public interface DishRepository extends CrudRepository<Dish, Long> {
	
}
