package br.com.peoplemanagement.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.peoplemanagement.domain.RoleModel;
import br.com.peoplemanagement.exception.RoleNotFoundExcepion;
import br.com.peoplemanagement.exception.UserNotFoundExcepion;
import br.com.peoplemanagement.service.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/api/admin")
@AllArgsConstructor
public class AdminController {
	
	private final UserService userService;
	
	@PatchMapping(path="/{id}")
	public void addRoleToUser(@PathVariable Long id, @RequestBody RoleModel role) throws UserNotFoundExcepion, RoleNotFoundExcepion {
		this.userService.addRoleToUser(id, role);
	}
}
