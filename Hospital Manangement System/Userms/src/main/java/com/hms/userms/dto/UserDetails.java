package com.hms.userms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String role;
	private String referenceId;
}
