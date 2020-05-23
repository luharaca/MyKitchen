package com.app.mykitchen.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.Role;
import com.app.mykitchen.domain.security.UserRole;
import com.app.mykitchen.domain.security.util.SecurityUtils;
import com.app.mykitchen.service.role.RoleService;
import com.app.mykitchen.service.user.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;

	User createUser(String username, String email, String password, List<String> roleNames) throws BusinessException {
		User user = new User();

		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(SecurityUtils.passwordEncoder().encode(password));
		user.setUserRoles(buildUserRoles(user, roleNames));

		return userService.createUser(user);
	}

	boolean userExists(String username, String email, Model model) {
		if (userService.findUserByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			model.addAttribute("username", username);
			return true;
		}

		if (userService.findUserByEmail(email) != null) {
			model.addAttribute("emailExists", true);
			model.addAttribute("email", email);
			return true;
		}

		return false;
	}

	String createUserToken() {
		// TODO: Generate a token for the user
		return UUID.randomUUID().toString();
	}

	private Set<UserRole> buildUserRoles(User user, List<String> roleNames) throws BusinessException {

		Set<UserRole> userRoleSet = new HashSet<>();

		Map<String, Role> nameAndRoleMap = buildNameAndRoleMap();

		for (String roleName : roleNames) {

			if (!nameAndRoleMap.containsKey(roleName)) {
				throw new BusinessException("Role " + roleName + " does not exist");
			}

			UserRole userRole = new UserRole(user, nameAndRoleMap.get(roleName));
			userRoleSet.add(userRole);
		}

		return userRoleSet;
	}

	private Map<String, Role> buildNameAndRoleMap() {
		Map<String, Role> nameAndRoleMap = new HashMap<>();

		List<Role> roles = roleService.findAllRoles();

		for (Role role : roles) {
			if (!nameAndRoleMap.containsKey(role.getName())) {
				nameAndRoleMap.put(role.getName(), role);
			}
		}

		return nameAndRoleMap;
	}

}
