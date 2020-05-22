package com.app.mykitchen.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.domain.User;


@Controller
public class HomeController {

	@Autowired
	UserController userController;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/myAccount")
	public String myAccount() {
		return "myAccount";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginActive", true);
		return "myAccount";
	}

	@RequestMapping(path = "/signup", method = RequestMethod.POST)
	public String createNewUserPost(HttpServletRequest request, @ModelAttribute("username") String username,
			@ModelAttribute("email") String email, Model model)  {

		model.addAttribute("createUserActive", true);
		model.addAttribute("username", username);
		model.addAttribute("email", email);
		
		if (userController.userExists(username,email, model)) {
			return "myAccount";
		}

		User user;
		
		try {
			user = userController.createUser(username, email, Arrays.asList("CUSTOMER"));
		} catch (BusinessException e) {
			return "myAccount";
		}
		
		model.addAttribute("user", user);

		// TODO: generate token for the new user

		// TODO: send an email to the user for the first time login

		return "myProfile";
	}

	@RequestMapping("/signup")
	public String createNewUser(Model model) {
		model.addAttribute("createUserActive", true);
		return "myAccount";
	}

	@RequestMapping("/forgetpassword")
	public String forgetPassword(Model model) {
		model.addAttribute("forgetPasswordActive", true);
		return "myAccount";
	}
}
