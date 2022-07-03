package com.jw.home.exception;

public class QuartzException extends CustomBusinessException {
	public static QuartzException INSTANCE = new QuartzException();

	QuartzException() {
		super();
		this.errorCode = 501;
		this.errorMessage = "Failed to schedule";
	}
}
