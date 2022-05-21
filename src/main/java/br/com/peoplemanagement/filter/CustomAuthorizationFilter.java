package br.com.peoplemanagement.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private final String secret;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(request.getServletPath().equals("/api/login")) {
			filterChain.doFilter(request, response);
			
		}else {
			String authorizationHeader = request.getHeader("Authorization");
			
			if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				
				try {
					
					String token = authorizationHeader.substring("Bearer ".length());
					
					Algorithm augorithm = Algorithm.HMAC256(this.secret.getBytes());
					JWTVerifier verifier = JWT.require(augorithm).build();
					
					DecodedJWT decodedJWT = verifier.verify(token);
					String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
					
					List<SimpleGrantedAuthority> authorities = new ArrayList<>();
					
					Stream.of(roles)
						.forEach(role -> {
							authorities.add(new SimpleGrantedAuthority(role));
						});
					
					SecurityContextHolder.getContext()
						.setAuthentication(new UsernamePasswordAuthenticationToken(roles, null, authorities));
				
					filterChain.doFilter(request, response);
					
				}catch(Exception e) {
					String message = e.getMessage();
					Integer statusCode = HttpStatus.FORBIDDEN.value();
					
					response.setContentType("application/json");
					response.setStatus(statusCode);
					
					Map<String, String> errors = new HashMap<>();
					errors.put("message", message);
					
					new ObjectMapper().writeValue(response.getOutputStream(), errors);
				}
				
			}else {
				filterChain.doFilter(request, response);
			}
			
		}
		
	}

}
