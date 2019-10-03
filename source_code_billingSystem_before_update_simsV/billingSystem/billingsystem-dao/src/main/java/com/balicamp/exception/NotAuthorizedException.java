package com.balicamp.exception;

public class NotAuthorizedException extends RuntimeException {

	private static final long serialVersionUID = 8799908972865787864L;

	public NotAuthorizedException(String message) {
		super("* SERVICE RUNTIME EXCEPTION : \n"+message);
	}

	public NotAuthorizedException(String message, Throwable throwable) {
		super("* SERVICE ERROR EXCEPTION : \n" + message, throwable);
	}

}
