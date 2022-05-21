package br.com.peoplemanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.peoplemanagement.domain.RoleModel;
import br.com.peoplemanagement.domain.UserModel;
import br.com.peoplemanagement.dto.UserDTO;
import br.com.peoplemanagement.exception.RoleNotFoundExcepion;
import br.com.peoplemanagement.exception.UserAlreadyExistsException;
import br.com.peoplemanagement.exception.UserNotFoundExcepion;
import br.com.peoplemanagement.repository.RoleRepository;
import br.com.peoplemanagement.repository.UserRepository;
import br.com.peoplemanagement.utils.MapperUtils;

@Service
public class UserService {
	
	private final String USER_ROLE = "USER";
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	
	private MapperUtils mapperUtils = new MapperUtils();
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	
	// ============================= [ Verify if the role exists ] =============================
	
	@Transactional
	private RoleModel verifyIfRoleExists(String roleName) throws RoleNotFoundExcepion{
		
		RoleModel role = this.roleRepository.findByName(roleName.toUpperCase());
		
		if(role == null) {
			throw new RoleNotFoundExcepion("Role not found");
		}
		
		return role;
	}
	
	// ============================= [verify if an user already exists ] =============================
	
	private void verifyIfUserAlreadyExists(String username) throws UserAlreadyExistsException {
		UserModel user = this.userRepository.findByUsername(username);
		
		if(user != null) {
			throw new UserAlreadyExistsException("Username already exists.");
		}
	}
	
	
	// ============================= [ Find User by username ] =============================
	
	public UserDTO findUserByUsername(String username) throws UserNotFoundExcepion {
		UserModel user = this.userRepository.findByUsername(username);
		
		if(user == null) {
			throw new UserNotFoundExcepion("User not found");
		}
		
		return this.mapperUtils.convertToDto(user);
	}
	
	// ============================= [Find user by id] =============================
	
	public UserDTO findUserById(Long id) throws UserNotFoundExcepion {
		UserModel user = this.userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundExcepion("User not found"));
		
		return this.mapperUtils.convertToDto(user);
	}
	
	
	// ============================= [ Save user ] =============================
	
	@Transactional
	public UserDTO save(UserDTO user) throws UserAlreadyExistsException, RoleNotFoundExcepion{

		
		this.verifyIfUserAlreadyExists(user.getUsername());
		
		UserModel userModel = this.mapperUtils.convertToEntity(user);
		
		userModel.getRoles().add(this.verifyIfRoleExists(USER_ROLE));
		
		userModel.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
	
		UserModel createdUser = this.userRepository.save(userModel);
		
		return this.mapperUtils.convertToDto(createdUser);
	}
	
	
	// ============================= [ List of Users ] =============================
	
	public List<UserDTO> findAllUsers() {
		List<UserModel> users = this.userRepository.findAll();
		
		return users.stream()
				.map(this.mapperUtils::convertToDto)
				.collect(Collectors.toList());
	}
	
	
	// ============================= [ Add role to user ] =============================
	
	@Transactional
	public void addRoleToUser(Long id, RoleModel role) throws UserNotFoundExcepion, RoleNotFoundExcepion {
		
		UserDTO userDTO = this.findUserById(id);
		
		RoleModel roleModel = this.verifyIfRoleExists(role.getName());
		
		UserModel userModel = this.mapperUtils.convertToEntity(userDTO);
		userModel.getRoles().add(roleModel);
		
		this.userRepository.save(userModel);
	}

	// ============================= [ Delete ] =============================
	
	@Transactional
	public void deleteAUser(Long id) throws UserNotFoundExcepion {
		
		if(this.findUserById(id) != null) {
			this.userRepository.deleteById(id);
		}
	}
	
}
