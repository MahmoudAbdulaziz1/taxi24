package com.taxi24.exception;

import java.io.Serializable;
import java.util.List;

public class ErrorMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String message;
	List<String> details;

	public ErrorMessage() {
		super();
	}

	public ErrorMessage(String message, List<String> details) {
		super();
		this.message = message;
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "ErrorMessage [message=" + message + ", details=" + details + "]";
	}

}
