package br.com.peoplemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RoleNotFoundExcepion extends Exception {

	private static final long serialVersionUID = 1L;

	public RoleNotFoundExcepion(String message) {
		super(message);
	}
}
