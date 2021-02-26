package com.app.mykitchen.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.mykitchen.domain.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(properties = { "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect" })
public class UserRepositoryTest {

	private static final String USERNAME = "TEST_NAME";
	private static final String FIRST_NAME = "Sam";
	private static final String EMAIL = "sam.smith@gmail.com";

	@Autowired
	private TestEntityManager testEntityManager;
	@Autowired
	private UserRepository userRepository;

	User user;

	@Before
	public void setUp() {
		user = buildUser();
		testEntityManager.persist(user);
		testEntityManager.flush();
	}

	@Test
	public void testFindUserByUsername() {

		// when
		User foundUser = userRepository.findByUsername(USERNAME);

		// then
		assertEquals("Actual user name :", USERNAME, foundUser.getUsername());
		assertEquals("Actual user first name :", FIRST_NAME, foundUser.getFirstName());
		assertEquals("Actual user first name :", EMAIL, foundUser.getEmail());
	}

	private User buildUser() {
		User user = new User();
		user.setUsername(USERNAME);
		user.setFirstName(FIRST_NAME);
		user.setEmail(EMAIL);
		return user;
	}
}
