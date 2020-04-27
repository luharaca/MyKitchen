package com.app.mykitchen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.app.mykitchen.domain.security.util.SecurityUtils;
import com.app.mykitchen.service.user.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	public WebSecurityConfig() {
		bcryptPasswordEncoder = new BCryptPasswordEncoder();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .authorizeRequests()
		    .antMatchers(SecurityUtils.PUBLIC_MATCHERS)
		    .permitAll()
		    .anyRequest()
		    .authenticated();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder);
	}
}
