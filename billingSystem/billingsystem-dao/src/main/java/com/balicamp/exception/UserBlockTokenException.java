package com.balicamp.exception;

import org.acegisecurity.AuthenticationException;

public class UserBlockTokenException extends AuthenticationException {

	private static final long serialVersionUID = 3299002341375205945L;

	public UserBlockTokenException(String message) {
		super(message);
	}

}
