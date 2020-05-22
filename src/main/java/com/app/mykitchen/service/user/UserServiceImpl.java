package com.app.mykitchen.service.user;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.Role;
import com.app.mykitchen.domain.security.UserRole;
import com.app.mykitchen.repository.UserRepository;
import com.app.mykitchen.service.role.RoleService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);

		if (null == user) {
			throw new UsernameNotFoundException("Username not found");
		}

		return user;
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User createUser(User user) throws BusinessException {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			throw new BusinessException("The user already exists");
		}
		
		return userRepository.save(user);
	}
}
