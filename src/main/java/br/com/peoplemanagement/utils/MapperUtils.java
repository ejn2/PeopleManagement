package br.com.peoplemanagement.utils;

import org.modelmapper.ModelMapper;

import br.com.peoplemanagement.domain.UserModel;
import br.com.peoplemanagement.dto.UserDTO;


public class MapperUtils {

	private ModelMapper modelMapper = new ModelMapper();

	public UserDTO convertToDto(UserModel userModel) {
		return this.modelMapper.map(userModel, UserDTO.class);
		
	}
	
	public UserModel convertToEntity(UserDTO userDto) {
		return this.modelMapper.map(userDto, UserModel.class);
		
	}
}
