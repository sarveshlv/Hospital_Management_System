package com.Capg.Authentication.Service;

import com.Capg.Authentication.Model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthenticationService {
    String generateToken(String email, Collection<? extends GrantedAuthority> authorites);

    Boolean validateToken(String token, String email);

    User logout(String email);
}
