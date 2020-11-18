package com.own.cms.exception;

public class UserNotfoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public UserNotfoundException(String message) {
	        super(message);
	}
	public UserNotfoundException (String message, Throwable cause) {
        super(message, cause);
    }
}
