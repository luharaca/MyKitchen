package com.app.mykitchen.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.mykitchen.domain.Dish;

@Repository
public interface DishRepository extends CrudRepository<Dish, Long> {

}
