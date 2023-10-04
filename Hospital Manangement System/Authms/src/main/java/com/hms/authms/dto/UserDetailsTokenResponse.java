package com.hms.authms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDetailsTokenResponse {
	private String type = "Bearer";
	private String token;
	private UserDetails userDetails;
	public UserDetailsTokenResponse(String token, UserDetails userDetails) {
		this.token = token;
		this.userDetails = userDetails;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserDetails {
		private String id;
		private String email;
		private String firstName;
		private String lastName;
		private String role;
		private Boolean loggedId;
		private String referenceId;
	}
}
