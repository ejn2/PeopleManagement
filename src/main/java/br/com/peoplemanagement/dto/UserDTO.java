package br.com.peoplemanagement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long id;
	
	@NotNull(message = "The fild 'name' is mandatory.")
	@NotBlank(message = "The fild 'name' is mandatory.")
	@Size(min = 3, max = 50, message="The filed 'name' must be between {min} and {max} characters long.")
	private String name;
	
	@NotNull(message = "The fild 'username' is mandatory.")
	@NotBlank(message = "The fild 'username' is mandatory.")
	@Size(min = 3, max = 50, message="The filed 'username' must be between {min} and {max} characters long.")
	private String username;
	
	@NotNull(message = "The fild 'password' is mandatory.")
	@NotBlank(message = "The fild 'password' is mandatory.")
	@Length(min=8, message = "The filed 'password' should not be less than {min} characters")
	private String password;

}