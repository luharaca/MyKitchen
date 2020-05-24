package com.app.mykitchen.common;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.app.mykitchen.domain.User;

@Component
public class MailConstructor {

	@Autowired
	private Environment environment;

	public SimpleMailMessage buildEmail(HttpServletRequest request, String token, User user,
			String password) {

		String emailText = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
				+ "/login?token=" + token + "\nPlease click this link to verify your email" 
				+ "\n your temporary password is " + password;

		String adminEmail = environment.getProperty("support.email");
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("Welcome to My Kitchen");
		email.setText(emailText);
		email.setFrom(adminEmail);

		return email;
	}
}
