package com.elevate.edw.sqlservercdc;

@SuppressWarnings("serial")
public class ParameterCheckException extends Exception {

	/**
	 * 
	 */
	public ParameterCheckException() {
		super();
	}

	/**
	 * @param message
	 */
	public ParameterCheckException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public ParameterCheckException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public ParameterCheckException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ParameterCheckException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
