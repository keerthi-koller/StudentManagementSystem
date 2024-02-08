package com.jsp.studentmanagementsystem1.exception;

public class StudentNotFoundByPhNoException extends RuntimeException {
	private String message;

	public StudentNotFoundByPhNoException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
