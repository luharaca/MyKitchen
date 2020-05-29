package com.app.mykitchen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.app.mykitchen.domain.security.util.SecurityUtils;
import com.app.mykitchen.service.user.UserService;

@Configuration
@EnableWebSecurity
@Order(1)
public class CustomerWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	public CustomerWebSecurityConfig() {
		bcryptPasswordEncoder = SecurityUtils.passwordEncoder();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .authorizeRequests()
		    .antMatchers(SecurityUtils.PUBLIC_MATCHERS)
		    .permitAll()
		    .anyRequest()
		    .authenticated();
		
		http
		    .csrf().disable().cors().disable()
		    .formLogin().failureUrl("/login?error").defaultSuccessUrl("/")
		    .loginPage("/login").permitAll()
		    .and()
		    .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		    .logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
		    .and()
		    .rememberMe();
		
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder);
	}
}
