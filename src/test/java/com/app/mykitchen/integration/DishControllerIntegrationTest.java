package com.app.mykitchen.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.mykitchen.domain.Dish;
import com.app.mykitchen.repository.DishRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect" })
public class DishControllerIntegrationTest {

	private static final String DISH_NAME = "TEST_NAME";
	private static final String DISH_DESCRIPTION = "TEST_DESCRIPTION";

	@Autowired
	private TestEntityManager testEntityManager;
	@Autowired
	private DishRepository dishRepository;

	Dish dish;

	@Before
	public void setUp() {
		dish = buildDish();
		testEntityManager.persist(dish);
		testEntityManager.flush();
	}

	@Test
	public void testFindAllDishes() {

		// when
		List<Dish> foundDishes = (List<Dish>) dishRepository.findAll();

		// then
		assertEquals("Actual dish name :", DISH_NAME, foundDishes.get(0).getName());
		assertEquals("Actual dish description :", DISH_DESCRIPTION, foundDishes.get(0).getDescription());
		assertTrue("Actual dish active status :", foundDishes.get(0).isActive());
	}

	@Test
	public void testFindById() {

		// when
		Optional<Dish> foundDish = dishRepository.findById((long) 1);

		// then
		assertEquals("Actual dish name :", DISH_NAME, foundDish.get().getName());
		assertEquals("Actual dish description :", DISH_DESCRIPTION, foundDish.get().getDescription());
		assertTrue("Actual dish active status :", foundDish.get().isActive());
	}

	private Dish buildDish() {
		Dish dish = new Dish();
		dish.setDescription(DISH_DESCRIPTION);
		dish.setName(DISH_NAME);
		dish.setActive(true);

		return dish;
	}
}
