package com.Capg.User.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Capg.User.DTO.SignInDTO;
import com.Capg.User.DTO.SignUpDTO;
import com.Capg.User.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DaoAuthenticationProvider authenticationProvider;
    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@Valid @RequestBody SignUpDTO signupobj, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: "+result.getAllErrors());
        } else if(!signupobj.getPassword().equals(signupobj.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Password and Confirm password are not same");
        }

        userService.save(signupobj);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signinUser(@Valid @RequestBody SignInDTO signinobj, BindingResult result) {
        if(result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: "+result.getAllErrors());
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signinobj.getEmailId(), signinobj.getPassword());

        try {
            Authentication authentication = authenticationProvider.authenticate(token);

            if(authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return ResponseEntity.ok("Sign in successful!");
            }


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Unauthorized access");
    }
}
