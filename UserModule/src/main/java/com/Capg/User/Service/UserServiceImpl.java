package com.Capg.User.Service;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.Model.User;
import com.Capg.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User save(SignUpDTO signupobj) {
        User user = new User(signupobj.getEmailId(), signupobj.getPassword(), signupobj.getRole());
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = userRepository.findByEmailId(emailId);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with emailId: "+emailId);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
