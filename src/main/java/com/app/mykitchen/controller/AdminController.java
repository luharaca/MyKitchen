package com.app.mykitchen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.util.SecurityUtils;

@Controller
public class AdminController {
	
	@Autowired
	private UserController userController;
	
	@GetMapping("/admin")
	public String adminHome(Model model) {
		return "adminHome";
	}

	@GetMapping(path = "/admin/login")
	public String adminLogin(Model model) {
		return "adminLogin";
	}
	
	@PostMapping(path = "/admin/login")
	public String adminLoginPost(@ModelAttribute("username") String username,
			@ModelAttribute("password") String password, Model model) {
		User user = userController.findUserByUsername(username);

		if (user == null || !userController.adminRoleAssignedToUser(user)
				|| !user.getPassword().equals(SecurityUtils.passwordEncoder().encode(password))) {
			model.addAttribute("invalidUser", true);
			return "adminLogin";
		}

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

		return "adminHome";
	}

}
