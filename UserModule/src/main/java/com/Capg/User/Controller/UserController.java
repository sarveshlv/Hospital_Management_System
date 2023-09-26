package com.Capg.User.Controller;

import com.Capg.User.DTO.UpdateUserDTO;
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

    @PostMapping("/signup")
    public User signupUser(@Valid @RequestBody SignUpDTO signupobj) {
        return userService.addUser(signupobj);
    }

    @PutMapping("/updateUser")
    public User updateUser(@Valid @RequestBody UpdateUserDTO updateUserDTO) throws UserNotFoundException {
        return userService.updateUser(updateUserDTO);
    }

    @GetMapping("/findByEmailId/{emailId}")
    public User getUserByEmailId(@PathVariable String emailId) throws UserNotFoundException {
        return userService.getUserByEmail(emailId);
    }

    @GetMapping("/addReference/{emailId}/{referenceId}")
    public User addReference(@PathVariable String emailId, @PathVariable String referenceId) {
        return userService.addReferenceId(emailId, referenceId);
    }


}
