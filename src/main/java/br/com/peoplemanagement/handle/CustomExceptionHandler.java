package br.com.peoplemanagement.handle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.peoplemanagement.exception.RoleNotFoundExcepion;
import br.com.peoplemanagement.exception.UserAlreadyExistsException;
import br.com.peoplemanagement.exception.UserNotFoundExcepion;
import br.com.peoplemanagement.utils.ExceptionDefaultResponse;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


	// ========================== [ User already exists ] ==========================
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	private ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
		
		return new ResponseEntity<>(
				new ExceptionDefaultResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
	}
	
	
	// ========================== [ Validations ] ==========================
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		
		Map<String, String> errors = new HashMap<>();
		
		ex.getFieldErrors().forEach(err -> {
			errors.put(err.getField(), err.getDefaultMessage());
		});

		return new ResponseEntity<>(Collections.singleton(errors), HttpStatus.BAD_REQUEST);
	}


	// ========================== [ Json parse ] ==========================
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String message = "JSON error: Invalid request body";
		
		return new ResponseEntity<>(
				new ExceptionDefaultResponse(HttpStatus.BAD_REQUEST.value(), message),
				HttpStatus.BAD_REQUEST
				);
		
	}
	
	
	// ========================== [ User not found ] ==========================
	
	@ExceptionHandler(UserNotFoundExcepion.class)
	protected ResponseEntity<Object> handleUserNotFoundExcepion(UserNotFoundExcepion ex) {
		
		return new ResponseEntity<>(
				new ExceptionDefaultResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
				HttpStatus.NOT_FOUND
				);
	}
	
	
	// ========================== [ Role not found ] ==========================
	
	@ExceptionHandler(RoleNotFoundExcepion.class)
	private ResponseEntity<Object> handleRoleNotFoundExcepion(RoleNotFoundExcepion ex) {
		
		return new ResponseEntity<>(
				new ExceptionDefaultResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR
				);
	}


	// ========================== [ Method not allowed ] ==========================
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return new ResponseEntity<>(
				new ExceptionDefaultResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.getMessage()),
				HttpStatus.METHOD_NOT_ALLOWED
				);
	}
}
