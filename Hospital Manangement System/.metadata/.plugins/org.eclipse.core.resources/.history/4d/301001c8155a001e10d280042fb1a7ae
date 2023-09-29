package com.hms.authms.dto;

import lombok.Data;

@Data
public class JwtTokenResponse {
	private String token;
	private String type = "Bearer";
	private String username;
	private String roles;

	public JwtTokenResponse(String token, String username, String roles) {
		this.token = token;
		this.username = username;
		this.roles = roles;
	}
}
