package br.com.peoplemanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.peoplemanagement.domain.UserModel;
import br.com.peoplemanagement.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocalUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel userModel = this.userRepository.findByUsername(username);
		
		if(userModel == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		userModel.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		
		User user = new User(userModel.getUsername(), userModel.getPassword(), authorities);
		
		return user;
		
	}

}
