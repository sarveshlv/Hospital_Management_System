package com.Capg.Authentication.Controller;

import com.Capg.Authentication.DTO.SignInDTO;
import com.Capg.Authentication.DTO.TokenResponseUser;
import com.Capg.Authentication.DTO.UserDetails;
import com.Capg.Authentication.Model.User;
import com.Capg.Authentication.Repository.UserRepository;
import com.Capg.Authentication.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/Authenticate")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/signin")
    public ResponseEntity<TokenResponseUser> getToken(HttpServletRequest request, @Valid @RequestBody SignInDTO signInDTO) {

        //Creates an authentication token using the provided email and password
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signInDTO.getEmail(),
                signInDTO.getPassword());

        //Used to authenticate the user's credentials
        Authentication authentication = authenticationManager.authenticate(token);

        //Stores the user's authenticated information
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        String email = authentication.getName().toString();
        Optional<User> user = userRepository.findByEmailId(email);
        Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();
        String jwtToken = authenticationService.generateToken(email, authorites);

        //This allows subsequent requests to access the user's security context.
        HttpSession session = request.getSession(true);
        session.setAttribute("USER_EMAIL", email);

        return ResponseEntity.ok(new TokenResponseUser(jwtToken, getUserDetails(user.get())));

    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token, HttpServletRequest request) {
        HttpSession session = request.getSession();

        String email = (String) session.getAttribute("USER_EMAIL");
        if (email != null) {
            return authenticationService.validateToken(token, email);
        }
        return false;
    }

    @GetMapping("/logout/{emailId}")
    public UserDetails logout(@PathVariable String emailId) {
        User user = authenticationService.logout(emailId);
        return getUserDetails(user);
    }

    private UserDetails getUserDetails(User user) {
        UserDetails obj = new UserDetails();
        obj.setUserId(user.getUserId());
        obj.setFirstName((user.getFirstName()));
        obj.setLastName((user.getLastName()));
        obj.setRole(user.getRole().toString());
        obj.setEmailId(user.getEmailId());
        obj.setReferenceId(user.getReferenceId());
        obj.setLoggedIn(user.getLoggedIn());
        return obj;
    }
}
