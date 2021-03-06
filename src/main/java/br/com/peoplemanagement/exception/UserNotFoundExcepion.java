package br.com.peoplemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundExcepion extends Exception {

	private static final long serialVersionUID = 1L;

	public UserNotFoundExcepion(String message) {
		super(message);
	}
}
