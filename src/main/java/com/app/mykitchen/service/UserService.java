package com.app.mykitchen.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.PasswordResetToken;

public interface UserService extends UserDetailsService {
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	User createUser(User user) throws BusinessException;
	
	User updateUser(User user) throws BusinessException; 
	
	PasswordResetToken getPasswordResetToken(String token);
	
	void createPasswordResetToken(PasswordResetToken passwordResetToken);
}
