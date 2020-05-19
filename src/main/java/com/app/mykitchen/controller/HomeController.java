package com.app.mykitchen.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
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
