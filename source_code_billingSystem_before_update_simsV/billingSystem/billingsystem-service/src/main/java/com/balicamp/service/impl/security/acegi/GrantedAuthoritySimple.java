package com.balicamp.service.impl.security.acegi;

import org.acegisecurity.GrantedAuthority;

public class GrantedAuthoritySimple implements GrantedAuthority {
	
	private static final long serialVersionUID = -1652741780823706975L;
	
	private String authority;

	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}

}
