package br.com.tdev.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.tdev.photoapp.api.users.service.UsersService;
import br.com.tdev.photoapp.api.users.shared.UserDto;
import br.com.tdev.photoapp.api.users.ui.model.LoginRequestModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	private UsersService userService;
	private Environment enviroment;
	
	public AuthenticationFilter(UsersService userService, Environment enviroment, AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.enviroment = enviroment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			
			LoginRequestModel creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);
			
			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(), 
							creds.getPassword(),
							new ArrayList<>()));
			
			
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		String userName = ((User) auth.getPrincipal()).getUsername();
		UserDto userDetails = userService.getUserDetailsByEmail(userName);
		
		String token = Jwts.builder()
				.setSubject(userDetails.getUserId())
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(enviroment.getProperty("token.expiration_time"))))
				.signWith(SignatureAlgorithm.HS512, enviroment.getProperty("token.secret"))
				.compact();
		
		response.addHeader("token", token);
		response.addHeader("userId", userDetails.getUserId());
	}
}
