package br.com.peoplemanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.peoplemanagement.domain.RoleModel;
import br.com.peoplemanagement.domain.UserModel;
import br.com.peoplemanagement.dto.UserDTO;
import br.com.peoplemanagement.exception.RoleNotFoundExcepion;
import br.com.peoplemanagement.exception.UserAlreadyExistsException;
import br.com.peoplemanagement.exception.UserNotFoundExcepion;
import br.com.peoplemanagement.repository.RoleRepository;
import br.com.peoplemanagement.repository.UserRepository;
import br.com.peoplemanagement.utils.MapperUtils;
import br.com.peoplemanagement.utils.UserUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	RoleRepository roleRepository;

	@InjectMocks
	UserService userService;
	
	MapperUtils mapperUtils = new MapperUtils();
	
	UserDTO userDTO = UserUtils.newUserDTO();
	RoleModel roleModel = UserUtils.newRole();
	
	
	//============================== [ Test | Save ] ==============================
	
	@Test
	void whenSAVEIsCalledWithValidData_ThenAUserShouldBeCreated() throws UserAlreadyExistsException, RoleNotFoundExcepion {
		
		UserModel user = this.mapperUtils.convertToEntity(this.userDTO);
		user.getRoles().add(this.roleModel);
		
		when(this.userRepository.findByUsername(this.userDTO.getUsername()))
			.thenReturn(null);
		
		when(this.roleRepository.findByName(this.roleModel.getName()))
			.thenReturn(this.roleModel);
		
		when(this.userRepository.save(any(UserModel.class)))
			.thenReturn(user);
		
		assertEquals(this.userDTO.getUsername(),
				this.userService.save(userDTO).getUsername());
	}
	
	//============================== [ Test | Find all users] ==============================
	
	@Test
	void whenFindAllUsersIsCalled_ThenAlistOfUsersShouldBeReturned() {
		
		when(this.userRepository.findAll())
			.thenReturn(Collections.singletonList(this.mapperUtils.convertToEntity(this.userDTO)));
		
		List<UserDTO> listOfUsers = this.userService.findAllUsers();
		
		
		assertEquals(this.userDTO,
				listOfUsers.get(0));
		
		assertEquals(this.userDTO.getUsername(),
				listOfUsers.get(0).getUsername());
		
		assertEquals(this.userDTO.getName(),
				listOfUsers.get(0).getName());
		
	}
	
	
	//============================== [ Test | Find by username ] ==============================
	
	@Test
	void whenFindByUsernameIsCalledWithVaidUsername_ThenAUserShouldBerReturned() throws UserNotFoundExcepion {
		
		when(this.userRepository.findByUsername(this.userDTO.getUsername()))
			.thenReturn(this.mapperUtils.convertToEntity(this.userDTO));
		
		UserDTO foundUserDTO = this.userService.findUserByUsername(this.userDTO.getUsername());
		
		assertEquals(this.userDTO.getUsername(),
				foundUserDTO.getUsername());
		
		assertEquals(this.userDTO.getName(),
				foundUserDTO.getName());
		
	}
	
	
	@Test
	void whenFindByUsernameIsCalledWithInvaidUsername_ThenAnExcepionShouldBeThrown() throws UserNotFoundExcepion {
		
		when(this.userRepository.findByUsername(this.userDTO.getUsername()))
			.thenReturn(null);
		
		assertThrows(UserNotFoundExcepion.class, () ->
				this.userService.findUserByUsername(this.userDTO.getUsername()));
		
	}
	
	//============================== [ Test | Delete ] ==============================
	
	@Test
	void whenDeleteAUserIsCalledWithValidId_ThenAUserShouldBeDeleted() throws UserNotFoundExcepion {
		
		UserModel user = this.mapperUtils.convertToEntity(this.userDTO);
				
		when(this.userRepository.findById(user.getId()))
			.thenReturn(Optional.of(user));
		
		doNothing().when(this.userRepository).deleteById(user.getId());
		
		this.userService.deleteAUser(user.getId());
		
		verify(this.userRepository, times(1)).deleteById(user.getId());
		
		
	}
	
	@Test
	void whenDeleteAUserIsCalledWithInvalidId_ThenAnExcepionShouldBeThrown() throws UserNotFoundExcepion {
		
		when(this.userRepository.findById(any(Long.class)))
			.thenReturn(Optional.empty());

		assertThrows(UserNotFoundExcepion.class, () ->
				this.userService.deleteAUser(this.userDTO.getId()));
	}
}
