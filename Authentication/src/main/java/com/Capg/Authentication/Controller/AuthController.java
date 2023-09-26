package com.Capg.Authentication.Controller;

import com.Capg.Authentication.DTO.JwtTokenResponse;
import com.Capg.Authentication.DTO.SignInDTO;
import com.Capg.Authentication.Service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/Authenticate")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/signin")
    public ResponseEntity<JwtTokenResponse> getToken(@Valid @RequestBody SignInDTO signInDTO) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(),
                signInDTO.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String email = authentication.getName().toString();
        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
        String jwtToken = authenticationService.generateToken(email, authorites);

        return ResponseEntity.ok(new JwtTokenResponse(jwtToken, email, authorites.toString()));

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        if (authenticationService.validateToken(token))
            return "Token is valid";
        else
            return "Token is invalid";
    }
}
