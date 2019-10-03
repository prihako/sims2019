package com.balicamp.exception;

import org.acegisecurity.AuthenticationException;

public class UserBlockException extends AuthenticationException {

	private static final long serialVersionUID = 3299002341375205945L;

	public UserBlockException(String message) {
		super(message);
	}

}
