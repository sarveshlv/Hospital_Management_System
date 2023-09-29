package com.hms.userms.service;

import com.hms.userms.dto.AddUserRequest;
import com.hms.userms.dto.UpdatePasswordRequest;
import com.hms.userms.dto.UpdateUserRequest;
import com.hms.userms.dto.UserDetails;
import com.hms.userms.exception.UserNotFoundException;

public interface IUserService {

	UserDetails saveUser(AddUserRequest addUserRequest);

	UserDetails updateUser(UpdateUserRequest updateUserRequest) throws UserNotFoundException;

	UserDetails getUserByEmail(String email) throws UserNotFoundException;

	UserDetails updatePassword(UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundException;

	UserDetails addReferenceId(String email, String referenceId) throws UserNotFoundException;

}
