package com.Capg.User.Service;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.DTO.UpdateUserDTO;
import com.Capg.User.DTO.UserDetails;
import com.Capg.User.Exception.UserAlreadyExistsException;
import com.Capg.User.Exception.UserNotFoundException;
import com.Capg.User.Model.User;
import com.Capg.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails addUser(SignUpDTO signupobj) {
        Optional<User> obj = userRepository.findByEmailId(signupobj.getEmailId());
        if(obj.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with this email ID");
        }
        User user = new User();
        user.setFirstName(signupobj.getFirstName());
        user.setLastName(signupobj.getLastName());
        user.setEmailId(signupobj.getEmailId());
        user.setRole(signupobj.getRole());
        user.setPassword(passwordEncoder.encode(signupobj.getPassword()));
        user.setLoggedIn(false);
        user = userRepository.save(user);
        return getDetails(user);
    }

    @Override
    public User updateUser(UpdateUserDTO updateUserDTO) throws UserNotFoundException {
        User user = getUser(updateUserDTO.getEmail());
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return getUser(email);
    }

    @Override
    public User addReferenceId(String emailId, String referenceId) throws UserNotFoundException {
        User user = getUser(emailId);
        user.setReferenceId(referenceId);
        return userRepository.save(user);
    }

    private User getUser(String emailId) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmailId(emailId);
        if(user.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: "+emailId);
        }
        return user.get();
    }

    private UserDetails getDetails(User user) {
        UserDetails obj = new UserDetails();
        obj.setUserId(user.getUserId());
        obj.setEmailId(user.getEmailId());
        obj.setFirstName(user.getFirstName());
        obj.setLastName(user.getLastName());
        obj.setRole(user.getRole());
        obj.setReferenceId(user.getReferenceId());
        return obj;
    }
}
