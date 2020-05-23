package com.app.mykitchen.controller;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.common.MailConstructor;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.util.SecurityUtils;

@Controller
public class HomeController {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static final String MY_ACCOUNT_TEMPLATE = "myAccount";
	private static final String SIGN_UP = "signup";

	@Autowired
	private UserController userController;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@GetMapping(path = "/")
	public String index() {
		return "index";
	}

	@GetMapping("/" + MY_ACCOUNT_TEMPLATE)
	public String myAccount() {
		return MY_ACCOUNT_TEMPLATE;
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginActive", true);
		return MY_ACCOUNT_TEMPLATE;
	}

	@PostMapping(path = "/" + SIGN_UP)
	public String createNewUserPost(HttpServletRequest request, @ModelAttribute("username") String username,
			@ModelAttribute("email") String email, Model model) {

		model.addAttribute("createUserActive", true);

		if (userController.userExists(username, email, model)) {
			return MY_ACCOUNT_TEMPLATE;
		}

		String password = SecurityUtils.randomPassword();

		User user;
		try {
			user = userController.createUser(username, email, password, Arrays.asList("CUSTOMER"));
		} catch (BusinessException e) {
			logger.error("User could not be created due to : " + e.getMessage());
			return MY_ACCOUNT_TEMPLATE;
		}

		sendEmailToNewUser(request, user, password, model);

		return MY_ACCOUNT_TEMPLATE;
	}

	@GetMapping("/" + SIGN_UP)
	public String createNewUser(Model model) {
		model.addAttribute("createUserActive", true);
		return MY_ACCOUNT_TEMPLATE;
	}

	@GetMapping("/forgetpassword")
	public String forgetPassword(Model model) {
		model.addAttribute("forgetPasswordActive", true);
		return MY_ACCOUNT_TEMPLATE;
	}

	private void sendEmailToNewUser(HttpServletRequest request, User user, String password, Model model) {

		String token = userController.createUserToken();

		SimpleMailMessage email = mailConstructor.buildEmailForNewUser(request, token, user, password);

		mailSender.send(email);

		model.addAttribute("emailSent", true);
	}
}
