package com.balicamp.exception;

import org.acegisecurity.AuthenticationException;

public class UserResetException extends AuthenticationException {

	private static final long serialVersionUID = 3299002341375205945L;

	public UserResetException(String message) {
		super(message);
	}

}
