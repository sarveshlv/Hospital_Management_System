package com.Capg.Authentication.Service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthenticationService {
    String generateToken(String email, Collection<? extends GrantedAuthority> authorites);

    Boolean validateToken(String token);
}
