package com.app.mykitchen.domain.security;

import org.springframework.security.core.GrantedAuthority;

public class ApplicationAuthority implements GrantedAuthority{

	private static final long serialVersionUID = 1146976482747630069L;
	
	private final String authority;
	
	public ApplicationAuthority(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return authority;
	}

}
