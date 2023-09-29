package com.hms.authms.controller;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hms.authms.dto.LoginRequest;
import com.hms.authms.entity.User;
import com.hms.authms.repository.UserRepository;
import com.hms.authms.dto.UserDetailsTokenResponse;
import com.hms.authms.dto.UserDetailsTokenResponse.UserDetails;

import com.hms.authms.service.IAuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;


	@PostMapping("/login")
	public ResponseEntity<UserDetailsTokenResponse> getToken(@Valid @RequestBody LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String email = authentication.getName().toString();
		Optional<User> user = userRepository.findByEmail(email);
		Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
		String jwtToken = authenticationService.generateToken(email, authorites);
		
		return ResponseEntity.ok(new UserDetailsTokenResponse(jwtToken, getUserDetailsResponse(user.get())));

	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam("token") String token) {
		if (authenticationService.validateToken(token))
			return "Token is valid";
		else
			return "Token is invalid";
	}
	
	public static UserDetails getUserDetailsResponse(User user) {

		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		userDetails.setEmail(user.getEmail());
		userDetails.setRole(user.getRole());
		userDetails.setReferenceId(user.getReferenceId());
		return userDetails;
	}
}