package com.app.mykitchen.domain.security.util;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
	
	private SecurityUtils(){
		// private constructor
	}
	
	public static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/img/**",
			"/",
			"/myAccount",
			"/login",
			"/signup",
			"/forgetpassword",
			"/admin/login",
			"/menu",
			"/dishDetails"
	};
	
	public static final String[] ADMIN_PUBLIC_MATCHER = {
			"/css/**",
			"/js/**",
			"/img/**",
			"/",
			"/admin/login"
	};
	
	private static final String SALT = "salt";
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}
	
	@Bean
	public static String randomPassword() {
		String Saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder salt = new StringBuilder();
		
		Random random = new Random();
		
		while(salt.length() < 18) {
			salt.append(Saltchars.charAt((int) (random.nextDouble() * Saltchars.length())));
		}
		
		return salt.toString();
	}
}
