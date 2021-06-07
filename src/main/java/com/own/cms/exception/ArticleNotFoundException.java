package com.own.cms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Article not found")
public class ArticleNotFoundException extends RuntimeException {
	
	public ArticleNotFoundException(String message) {
        super(message);
	}
	public ArticleNotFoundException (String message, Throwable cause) {
	    super(message, cause);
	}
}
