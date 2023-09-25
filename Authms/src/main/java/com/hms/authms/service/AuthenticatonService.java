package com.hms.authms.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import com.hms.authms.jwt.JwtUtility;

@Service
public class AuthenticatonService implements IAuthenticationService{
	

	@Autowired
	private JwtUtility jwtService;
	
	
	@Override
	public String generateToken(String email, Collection<? extends GrantedAuthority> authorites) {
		return jwtService.generateToken(email, authorites);
	}

	@Override
	public Boolean validateToken(String token) {
		return jwtService.validateToken(token);
	}
}