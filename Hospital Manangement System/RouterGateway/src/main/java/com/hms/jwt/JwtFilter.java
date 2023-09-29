package com.hms.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtFilter extends AbstractGatewayFilterFactory<JwtFilter.Config> {

	@Autowired
	private RouteValidator validator;

	@Autowired
	private JwtUtility jwtUtil;

	private static final String BEARER_PREFIX = "Bearer ";

	public static class Config {

	}

	public JwtFilter() {
		super(Config.class);
	}

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
				try {
					jwtUtil.validateToken(authHeader);
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to the application",
							e);
				}
			}
			return chain.filter(exchange);
		});
	}
}