package com.Capg.Authentication.Service;

import com.Capg.Authentication.JWT.JwtUtility;
import com.Capg.Authentication.Model.User;
import com.Capg.Authentication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private JwtUtility jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public String generateToken(String email, Collection<? extends GrantedAuthority> authorites) {
        Optional<User> user = userRepository.findByEmailId(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found for email ID: "+email);
        }
        User obj = user.get();
        obj.setLoggedIn(true);
        userRepository.save(obj);

        return jwtService.generateToken(email, authorites);
    }

    @Override
    public Boolean validateToken(String token, String email) {
        return jwtService.validateToken(token, email);
    }

    @Override
    public User logout(String email) {
        Optional<User> user = userRepository.findByEmailId(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found for email ID: "+email);
        }
        User obj = user.get();
        obj.setLoggedIn(false);
        userRepository.save(obj);
        return obj;
    }
}
