package com.hms.userms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.userms.dto.AddUserRequest;
import com.hms.userms.dto.UpdatePasswordRequest;
import com.hms.userms.dto.UpdateUserRequest;
import com.hms.userms.dto.UserDetails;
import com.hms.userms.exception.UserNotFoundException;
import com.hms.userms.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public UserDetails addNewUser(@Valid @RequestBody AddUserRequest registerRequest) {
		return userService.saveUser(registerRequest);
	}

	@PutMapping("/update")
	public UserDetails updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest)
			throws UserNotFoundException {
		return userService.updateUser(updateUserRequest);
	}

	@GetMapping("/findByEmail/{email}")
	public UserDetails getUserByEmail(@PathVariable String email) throws UserNotFoundException {
		return userService.getUserByEmail(email);
	}

	@PutMapping("/updatePassword")
	public UserDetails updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundException {
		return userService.updatePassword(updatePasswordRequest);
	}
	@GetMapping("/addReference/{emailId}/{referenceId}")
	public UserDetails addReference(@PathVariable String emailId, @PathVariable String referenceId) {
		return userService.addReferenceId(emailId, referenceId);
	}
}