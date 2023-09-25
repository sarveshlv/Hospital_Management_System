package com.hms.authms.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hms.authms.dto.LoginRequest;
import com.hms.authms.dto.JwtTokenResponse;
import com.hms.authms.service.IAuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private IAuthenticationService authenticationService;

	@Autowired
	private AuthenticationManager authenticationManager;


	@PostMapping("/login")
	public ResponseEntity<JwtTokenResponse> getToken(@Valid @RequestBody LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
				loginRequest.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String email = authentication.getName().toString();
		Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
		String jwtToken = authenticationService.generateToken(email, authorites);

		return ResponseEntity.ok(new JwtTokenResponse(jwtToken, email, authorites.toString()));

	}

	@GetMapping("/validate")
	public String validateToken(@RequestParam("token") String token) {
		if (authenticationService.validateToken(token))
			return "Token is valid";
		else
			return "Token is invalid";
	}
}