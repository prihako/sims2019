package com.balicamp.exception;

/**
 * @author <a href="mailto:aananto@balicamp.com">Arif Puji Ananto</a>
 */
public class TransactionSecurityException extends RuntimeException {

	private static final long serialVersionUID = 1389423478795580035L;

	public TransactionSecurityException() {

	}

	public TransactionSecurityException(String message) {
		super(message);
	}

	public TransactionSecurityException(String message, Throwable cause) {
		super(message, cause);
	}
}
