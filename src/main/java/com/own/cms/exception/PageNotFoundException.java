package com.own.cms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "page not found")
public class PageNotFoundException extends RuntimeException {

	public PageNotFoundException(String message) {
        super(message);
	}
	public PageNotFoundException (String message, Throwable cause) {
	    super(message, cause);
	}
}
