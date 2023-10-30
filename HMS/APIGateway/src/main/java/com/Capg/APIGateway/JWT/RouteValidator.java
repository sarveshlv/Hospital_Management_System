package com.Capg.APIGateway.JWT;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of("/User/signup", "/Authenticate/signin", "/Authenticate/validate", "/eureka");

    //Verify ServerHttpRequest is secure or not
    public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
