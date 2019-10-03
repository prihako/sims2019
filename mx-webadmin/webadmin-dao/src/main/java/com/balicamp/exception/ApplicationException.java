package com.balicamp.exception;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1389423478795580035L;

	public ApplicationException() {

	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message + "," + cause.getMessage() , cause);
	}
}
