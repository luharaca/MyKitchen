package com.app.mykitchen.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.repository.DishRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class DishControllerIntegrationTest {

	@Autowired
	private TestEntityManager testEntityManager;
	@Autowired
	private DishRepository dishRepository;

	@Ignore
	@Test
	public void testDishRepository() {

		// given
		Dish dish = buildDish();
		testEntityManager.persist(dish);
		testEntityManager.flush();

		// when
		Dish foundDish = dishRepository.findById((long) 123).get();

		// then
		// assertThat(foundDish.getName()).isEqualTo(foundDish.getName());
		assertEquals(dish.getName(), foundDish.getName());
	}

	private Dish buildDish() {
		Dish dish = new Dish();
		dish.setId((long) 123);
		dish.setDescription("Test Description");
		dish.setName("Test Name");
		return dish;
	}
}
