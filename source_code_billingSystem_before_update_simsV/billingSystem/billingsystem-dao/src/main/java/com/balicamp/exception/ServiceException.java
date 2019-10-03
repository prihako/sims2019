package com.balicamp.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 8799908972865787864L;

	public ServiceException(String message) {
		super("* SERVICE RUNTIME EXCEPTION : \n"+message);
	}

	public ServiceException(String message, Throwable throwable) {
		super("* SERVICE ERROR EXCEPTION : \n" + message, throwable);
	}

}
