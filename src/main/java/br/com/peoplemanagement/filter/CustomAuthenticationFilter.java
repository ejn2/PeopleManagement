package br.com.peoplemanagement.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authManager;
	private final String secret;
	private Integer expiresAt;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = null;
		String password = null;
		
		try {
			Map<String, String> data = new ObjectMapper().readValue(request.getInputStream(), Map.class);
			
			username = data.get("username");
			password = data.get("password");
			
		} catch (IOException ex) {
			throw new AuthenticationServiceException(ex.getMessage(), ex);
		}
		
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		
		return this.authManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User user = (User) authResult.getPrincipal();
		
		Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
		
		String token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + this.expiresAt * (60 * 1000)))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities()
						.stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
						.sign(algorithm);
		
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("access_token", token);
		
		new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
	}

}
