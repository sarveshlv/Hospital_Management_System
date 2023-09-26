package com.Capg.User.Service;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.DTO.UpdateUserDTO;
import com.Capg.User.Exception.UserNotFoundException;
import com.Capg.User.Model.User;

public interface UserService {

    User addUser(SignUpDTO signupobj);

    User updateUser(UpdateUserDTO updateUserDTO) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    User addReferenceId(String email, String referenceId) throws UserNotFoundException;
}
