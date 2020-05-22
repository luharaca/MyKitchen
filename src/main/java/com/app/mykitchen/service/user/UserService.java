package com.app.mykitchen.service.user;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;

public interface UserService extends UserDetailsService {
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	User createUser(User user) throws BusinessException;
}
