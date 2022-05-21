package br.com.peoplemanagement.utils;

import br.com.peoplemanagement.domain.RoleModel;
import br.com.peoplemanagement.dto.UserDTO;

public abstract class UserUtils {

	public static UserDTO newUserDTO() {
		UserDTO user = new UserDTO();
		user.setId(1L);
		user.setName("Maria");
		user.setUsername("maria");
		user.setPassword("12345678");
		
		return user;
	}
	
	public static RoleModel newRole() {
		RoleModel role = new RoleModel();
		
		role.setId(1L);
		role.setName("USER");
		
		return role;
	}
	
}
