package com.app.mykitchen.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.mykitchen.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUsername(String username);
	
	User findByEmail(String email);
}
