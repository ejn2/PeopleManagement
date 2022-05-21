package br.com.peoplemanagement.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.peoplemanagement.dto.UserDTO;
import br.com.peoplemanagement.repository.RoleRepository;
import br.com.peoplemanagement.repository.UserRepository;
import br.com.peoplemanagement.service.LocalUserDetailsService;
import br.com.peoplemanagement.service.UserService;
import br.com.peoplemanagement.utils.UserUtils;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	private final String API_URL = "http://localhost:8080/api";
	
	@MockBean
	UserService userService;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	RoleRepository roleRepository;
	
	@MockBean
	LocalUserDetailsService localUserDetailsService;
	
	@MockBean
	BCryptPasswordEncoder BCryptPasswordEncoder;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	UserDTO userDTO = UserUtils.newUserDTO();

	@Test
	void whenSaveIsCalled_ThenAUserIsSaved() throws Exception{
		
		String body = this.objectMapper.writeValueAsString(this.userDTO);
		
		when(this.userService.save(any(UserDTO.class)))
			.thenReturn(this.userDTO);
		
		this.mockMvc.perform(post(this.API_URL + "/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(this.userDTO.getName())))
			.andExpect(jsonPath("$.username", is(this.userDTO.getUsername())));
	}
}
