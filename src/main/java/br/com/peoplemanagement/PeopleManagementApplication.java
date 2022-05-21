package br.com.peoplemanagement;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.peoplemanagement.domain.RoleModel;
import br.com.peoplemanagement.domain.UserModel;
import br.com.peoplemanagement.repository.RoleRepository;
import br.com.peoplemanagement.repository.UserRepository;

@SpringBootApplication
public class PeopleManagementApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PeopleManagementApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserRepository userRepo, RoleRepository roleRepo) {
		
		return args -> {
			
			RoleModel adminRole = new RoleModel(null, "ADMIN");
			RoleModel userRole = new RoleModel(null, "USER");
			
			roleRepo.save(adminRole);
			roleRepo.save(userRole);
			
			RoleModel role = roleRepo.findByName("ADMIN");
			
			if(role != null) {
				
				UserModel admin = new UserModel();
				admin.setName("Admin");
				admin.setUsername("admin");
				admin.setPassword(this.passwordEncoder().encode("12345678"));
				admin.getRoles().add(role);
	
				userRepo.save(admin);
			}
			
		};
		
	}
}
