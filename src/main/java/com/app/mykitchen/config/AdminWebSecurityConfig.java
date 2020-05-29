package com.app.mykitchen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.app.mykitchen.domain.security.util.SecurityUtils;


@Configuration
@EnableWebSecurity
@Order(2)
public class AdminWebSecurityConfig extends WebSecurityConfigurerAdapter {
	


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	     .authorizeRequests()
	     .antMatchers(SecurityUtils.ADMIN_PUBLIC_MATCHER).permitAll()
	     .anyRequest()
	     .authenticated();
		
		http.csrf().disable().cors().disable()
		  .formLogin().failureUrl("/admin/login?error").defaultSuccessUrl("/admin")
		  .loginPage("/admin/login").permitAll().and()
		  .logout()
		  .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		  .logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
		  .and()
		  .rememberMe();
	}
}
