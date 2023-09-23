package com.hms.authms.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public interface IAuthenticationService {

	String generateToken(String email, Collection<? extends GrantedAuthority> authorites);

	Boolean validateToken(String token);
}
