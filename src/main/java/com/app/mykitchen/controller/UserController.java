package com.app.mykitchen.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	User createUser(String username, String email, List<String> roleNames) throws BusinessException {
		User user = new User();

		user.setEmail(email);
		user.setUsername(username);
		// generate a temporary password for the new user
		user.setPassword(SecurityUtils.passwordEncoder().encode(SecurityUtils.randomPassword()));
		user.setUserRoles(buildUserRoles(user, roleNames));

		return userService.createUser(user);
	}

	boolean userExists(String username, String email, Model model) {
		if (userService.findUserByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return true;
		}

		if (userService.findUserByEmail(email) != null) {
			model.addAttribute("emailExists", true);
			return true;
		}

		return false;
	}

	private Set<UserRole> buildUserRoles(User user, List<String> roleNames) throws BusinessException {

		Set<UserRole> userRoleSet = new HashSet<>();

		Map<String, Role> nameAndRoleMap = buildNameAndRoleMap(roleNames);

		for (String roleName : roleNames) {

			if (!nameAndRoleMap.containsKey(roleName)) {
				throw new BusinessException("Role " + roleName + " does not exist");
			}

			UserRole userRole = new UserRole(user, nameAndRoleMap.get(roleName));
			userRoleSet.add(userRole);
		}

		return userRoleSet;
	}

	private Map<String, Role> buildNameAndRoleMap(List<String> roleNames) {
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
