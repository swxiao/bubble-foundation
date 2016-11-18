package com.bubble.foundation.hbase.exception;

public class HbaseException extends RuntimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5254406651707710682L;

	public HbaseException() {
		super();
	}

	public HbaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HbaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public HbaseException(String message) {
		super(message);
	}

	public HbaseException(Throwable cause) {
		super(cause);
	}

	
}
