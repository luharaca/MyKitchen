package com.app.mykitchen.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.mykitchen.common.BusinessException;
import com.app.mykitchen.common.MailConstructor;
import com.app.mykitchen.domain.User;
import com.app.mykitchen.domain.security.PasswordResetToken;
import com.app.mykitchen.domain.security.util.SecurityUtils;

@Controller
public class HomeController {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static final String MY_ACCOUNT = "myAccount";
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

	@GetMapping(path = "/admin")
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

	@GetMapping("/" + MY_ACCOUNT)
	public String myAccount() {
		return MY_ACCOUNT;
	}

	@GetMapping("/login")
	public String login(@RequestParam(name = "token", required = false) String token, Model model) {

		if (token == null) {
			model.addAttribute("loginActive", true);
			return MY_ACCOUNT;
		}

		PasswordResetToken passwordResetToken = userController.getPasswordResetToken(token);

		if (isInvalidToken(passwordResetToken)) {
			model.addAttribute("message", "The token is invalid");
			return "redirect:/badRequest";
		}

		Authentication auth = userController.getUserAuthentication(passwordResetToken, model);
	
		SecurityContextHolder.getContext()
				.setAuthentication(userController.getUserAuthentication(passwordResetToken, model));

		model.addAttribute("editActive", true);

		return "myProfile";
	}

	@GetMapping("/" + SIGN_UP)
	public String createNewUser(Model model) {
		model.addAttribute("createUserActive", true);
		return MY_ACCOUNT;
	}

	@PostMapping(path = "/" + SIGN_UP)
	public String createNewUserPost(HttpServletRequest request, @ModelAttribute("username") String username,
			@ModelAttribute("email") String email, Model model) {

		model.addAttribute("createUserActive", true);

		if (userController.userExists(username, email, model)) {
			return MY_ACCOUNT;
		}

		String newPassword = SecurityUtils.randomPassword();

		User user;
		try {
			user = userController.createUser(username, email, newPassword, Arrays.asList("CUSTOMER"));
		} catch (BusinessException e) {
			logger.error("User could not be created due to : " + e.getMessage());
			return MY_ACCOUNT;
		}

		sendEmailToNewUser(request, user, newPassword);

		model.addAttribute("emailSent", true);

		return MY_ACCOUNT;
	}

	@GetMapping("/forgetpassword")
	public String forgetPassword(Model model) {
		model.addAttribute("forgetPasswordActive", true);
		return MY_ACCOUNT;
	}

	@PostMapping("/forgetpassword")
	public String forgetPasswordPost(HttpServletRequest request, @ModelAttribute("email") String email, Model model)
			throws BusinessException {
		model.addAttribute("forgetPasswordActive", true);

		User user = userController.findUserByEmail(email);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			model.addAttribute("email", email);
			return MY_ACCOUNT;
		}

		String newPassword = SecurityUtils.randomPassword();

		user.setPassword(SecurityUtils.passwordEncoder().encode(newPassword));

		user = userController.updateUser(user);

		sendEmailToNewUser(request, user, newPassword);

		model.addAttribute("newPasswordSent", true);

		return MY_ACCOUNT;
	}

	private void sendEmailToNewUser(HttpServletRequest request, User user, String password) {

		String token = UUID.randomUUID().toString();

		userController.createUserToken(user, token);

		SimpleMailMessage email = mailConstructor.buildEmail(request, token, user, password);

		mailSender.send(email);
	}

	private boolean isInvalidToken(PasswordResetToken passwordResetToken) {
		return passwordResetToken == null || passwordResetToken.getUser() == null
				|| new Date().after(passwordResetToken.getExpiryDate());
	}
}
