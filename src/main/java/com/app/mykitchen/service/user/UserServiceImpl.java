package com.app.mykitchen.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.mykitchen.domain.User;
import com.app.mykitchen.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		
		if(null == user) {
			throw new UsernameNotFoundException("Username not found");
		}
		
		return user;
	}
}
