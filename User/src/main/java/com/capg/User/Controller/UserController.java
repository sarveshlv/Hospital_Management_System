package com.capg.User.Controller;

import com.capg.User.JSONResponse.JSONResponse;
import com.capg.User.JwtConfig.JwtUtility;
import com.capg.User.LoginRequest.LoginRequest;
import com.capg.User.Model.User;
import com.capg.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userdetails= (UserDetails) authentication.getPrincipal();

        String token = jwtUtility.generateToken(authentication);

        Collection<? extends GrantedAuthority> authorities= userdetails.getAuthorities();
        List<String> list= authorities.stream().map(t -> t.getAuthority()).collect(Collectors.toList());

        JSONResponse resp=new JSONResponse(token, userdetails.getUsername(), list) ;

        return ResponseEntity.ok(resp);

    }

}
