package br.com.peoplemanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.peoplemanagement.dto.UserDTO;
import br.com.peoplemanagement.exception.RoleNotFoundExcepion;
import br.com.peoplemanagement.exception.UserAlreadyExistsException;
import br.com.peoplemanagement.exception.UserNotFoundExcepion;
import br.com.peoplemanagement.service.UserService;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO saveUser(@Valid @RequestBody UserDTO userDTO) throws UserAlreadyExistsException, RoleNotFoundExcepion {
		return this.userService.save(userDTO);
	}
	
	@GetMapping
	public List<UserDTO> list() {
		return this.userService.findAllUsers();
	}
	
	@GetMapping(path="/{id}")
	public UserDTO findUserById(@PathVariable Long id) throws UserNotFoundExcepion {
		return this.userService.findUserById(id);
	}
	
	@DeleteMapping(path="/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) throws UserNotFoundExcepion {
		this.userService.deleteAUser(id);
	}
}