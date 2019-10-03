package com.balicamp.exception;

import org.acegisecurity.AuthenticationException;

public class UserBlockAdminException extends AuthenticationException {

	private static final long serialVersionUID = 3299002341375205945L;

	public UserBlockAdminException(String message) {
		super(message);
	}

}
