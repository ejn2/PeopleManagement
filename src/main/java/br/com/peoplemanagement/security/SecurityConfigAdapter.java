package br.com.peoplemanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.peoplemanagement.filter.CustomAuthenticationFilter;
import br.com.peoplemanagement.filter.CustomAuthorizationFilter;
import br.com.peoplemanagement.service.LocalUserDetailsService;
import lombok.Setter;

@Configuration
@EnableWebSecurity
public class SecurityConfigAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private LocalUserDetailsService userService;
	private BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@Value("${jwt.secret}")
	@Setter
	private String secret;
	
	@Value("${jwt.expiresAt}") //Minutes
	@Setter
	private Integer expiresAt;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userService).passwordEncoder(this.bcryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		CustomAuthenticationFilter authFilter = new CustomAuthenticationFilter(
				this.authenticationManagerBean(),
				this.secret,
				this.expiresAt
				);
		
		authFilter.setFilterProcessesUrl("/api/login");
		
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "/api/login").permitAll()
			.antMatchers(HttpMethod.POST, "/api/user").permitAll()
			.antMatchers("/actuator/**").hasAnyAuthority("ADMIN")
			.antMatchers("/api/user/**").hasAnyAuthority("ADMIN")
		    .antMatchers("/api/admin/**").hasAnyAuthority("ADMIN");
		
		http.addFilter(authFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(this.secret), UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
