package com.Capg.User.Controller;

import com.Capg.User.DTO.UpdateUserDTO;
import com.Capg.User.DTO.UserDetails;
import com.Capg.User.Exception.UserNotFoundException;
import com.Capg.User.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    //Method to register a new user into the database, returns a custom UserDetails object
    @PostMapping("/signup")
    public UserDetails signupUser(@Valid @RequestBody SignUpDTO signupobj) {
        return userService.addUser(signupobj);
    }

    //Update the user details, returns the updated User object
    @PutMapping("/updateUser")
    public User updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) throws UserNotFoundException {
        return userService.updateUser(updateUserDTO);
    }

    //Find user by email ID, returns the user object
    @GetMapping("/findByEmailId/{emailId}")
    public User getUserByEmailId(@PathVariable String emailId) throws UserNotFoundException {
        return userService.getUserByEmail(emailId);
    }

    //Link the ID of patient/hospital to the user, returns the updated user object
    @GetMapping("/addReference/{emailId}/{referenceId}")
    public User addReference(@PathVariable String emailId, @PathVariable String referenceId) {
        return userService.addReferenceId(emailId, referenceId);
    }

}
