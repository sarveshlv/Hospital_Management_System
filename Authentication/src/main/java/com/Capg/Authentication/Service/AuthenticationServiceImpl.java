package com.Capg.Authentication.Service;

import com.Capg.Authentication.JWT.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class AuthenticationServiceImpl implements AuthenticationService{

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
