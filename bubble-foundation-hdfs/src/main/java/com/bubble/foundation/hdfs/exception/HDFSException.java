package com.bubble.foundation.hdfs.exception;

public class HDFSException extends RuntimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4233922104999571083L;

	public HDFSException() {
		super();
 	}

	public HDFSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HDFSException(String message, Throwable cause) {
		super(message, cause);
	}

	public HDFSException(String message) {
		super(message);
	}

	public HDFSException(Throwable cause) {
		super(cause);
	}

	
	
}
