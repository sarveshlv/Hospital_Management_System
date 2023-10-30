package com.Capg.APIGateway.JWT;

import com.Capg.APIGateway.Entity.User;
import com.Capg.APIGateway.UserRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtility jwtUtil;

    @Autowired
    private UserRepository userRepository;

    private static final String BEARER_PREFIX = "Bearer ";

    public static class Config {

    }

    public JwtFilter() {
        super(Config.class);
    }

    //Check the header format and existence
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                HttpHeaders headers = exchange.getRequest().getHeaders();

                if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }
                String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(BEARER_PREFIX.length());
                }

                String emailFromToken = jwtUtil.getUsernameFromToken(authHeader);
                Optional<User> user = userRepository.findByEmailId(emailFromToken);
                if(user.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user found!");
                }
                User obj = user.get();
                if(jwtUtil.isTokenExpired(authHeader)) {
                    throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Token expired");
                }
                if(!obj.isLoggedIn()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in!");
                }
            }
            return chain.filter(exchange);
        });
    }
}
