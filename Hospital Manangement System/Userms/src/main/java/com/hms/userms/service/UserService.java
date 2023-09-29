package com.hms.userms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.userms.dto.AddUserRequest;
import com.hms.userms.dto.UpdatePasswordRequest;
import com.hms.userms.dto.UpdateUserRequest;
import com.hms.userms.dto.UserDetails;
import com.hms.userms.entity.User;
import com.hms.userms.exception.UserNotFoundException;
import com.hms.userms.repository.UserRepository;

@Service
public class UserService implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails saveUser(AddUserRequest addUserRequest) {
		User user = new User();
		user.setFirstName(addUserRequest.getFirstName());
		user.setLastName(addUserRequest.getLastName());
		user.setEmail(addUserRequest.getEmail());
		user.setRole(addUserRequest.getRole());
		user.setPassword(passwordEncoder.encode(addUserRequest.getPassword()));

		user = userRepository.save(user);
		return getUserDetailsResponse(user);
	}

	@Override
	public UserDetails updateUser(UpdateUserRequest updateUserRequest) throws UserNotFoundException {
		User user = getUser(updateUserRequest.getEmail());

		user.setFirstName(updateUserRequest.getFirstName());
		user.setLastName(updateUserRequest.getLastName());
		user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
		userRepository.save(user);

		user = userRepository.save(user);
		return getUserDetailsResponse(user);
	}

	@Override
	public UserDetails getUserByEmail(String email) throws UserNotFoundException {
		User user = getUser(email);
		return getUserDetailsResponse(user);
	}

	@Override
	public UserDetails addReferenceId(String email, String referenceId) throws UserNotFoundException {
		User user = getUser(email);
		user.setReferenceId(referenceId);

		user = userRepository.save(user);
		return getUserDetailsResponse(user);
	}

	private User getUser(String email) throws UserNotFoundException {
		return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
	}

	public static UserDetails getUserDetailsResponse(User user) {

		UserDetails userDetails = new UserDetails();
		userDetails.setId(user.getId());
		userDetails.setFirstName(user.getFirstName());
		userDetails.setLastName(user.getLastName());
		userDetails.setEmail(user.getEmail());
		userDetails.setRole(user.getRole());
		userDetails.setReferenceId(user.getReferenceId());
		return userDetails;
	}

	@Override
	public UserDetails updatePassword( UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundException {
		User user = getUser(updatePasswordRequest.getEmail());
		user.setLastName(updatePasswordRequest.getPassword());
		return getUserDetailsResponse(user);
	}
}