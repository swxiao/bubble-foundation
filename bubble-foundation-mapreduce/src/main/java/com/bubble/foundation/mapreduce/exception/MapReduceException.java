package com.bubble.foundation.mapreduce.exception;

public class MapReduceException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 9092530956277010420L;

	public MapReduceException() {
		super();
	}

	public MapReduceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MapReduceException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapReduceException(String message) {
		super(message);
	}

	public MapReduceException(Throwable cause) {
		super(cause);
	}

	
}
