package com.jsp.studentmanagementsystem1.exception;

public class StudentEmailsNotFoundBasedOnGradeException extends RuntimeException {

	private String message;

	public StudentEmailsNotFoundBasedOnGradeException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
