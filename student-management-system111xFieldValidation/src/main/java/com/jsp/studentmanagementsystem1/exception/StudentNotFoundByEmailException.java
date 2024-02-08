package com.jsp.studentmanagementsystem1.exception;

public class StudentNotFoundByEmailException extends RuntimeException{
	private String message;

	public String getMessage() {
		return message;
	}

	public StudentNotFoundByEmailException(String message) {
		super();
		this.message = message;
	}
}
