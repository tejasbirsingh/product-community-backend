package com.trainingproject.backend.exceptions;

public class ProductWebsiteException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductWebsiteException(String exMessage, Exception exception) {
		super(exMessage, exception);
	}

	public ProductWebsiteException(String exMessage) {
		super(exMessage);
	}
}
