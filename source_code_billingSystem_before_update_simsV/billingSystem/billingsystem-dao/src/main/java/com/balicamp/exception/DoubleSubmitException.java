package com.balicamp.exception;

public class DoubleSubmitException extends RuntimeException {

	private static final long serialVersionUID = 7890737717999346666L;

	public DoubleSubmitException() {
	}

	public DoubleSubmitException(String message) {
		super(message);
	}

	public DoubleSubmitException(String message, Throwable cause) {
		super(message, cause);
	}
}
