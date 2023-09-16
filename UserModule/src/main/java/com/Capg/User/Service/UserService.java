package com.Capg.User.Service;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(SignUpDTO signupobj);
}
