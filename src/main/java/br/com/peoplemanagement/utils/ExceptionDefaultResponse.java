package br.com.peoplemanagement.utils;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExceptionDefaultResponse {

	private Integer statusCode;
	private String message;
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public ExceptionDefaultResponse(Integer statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
}