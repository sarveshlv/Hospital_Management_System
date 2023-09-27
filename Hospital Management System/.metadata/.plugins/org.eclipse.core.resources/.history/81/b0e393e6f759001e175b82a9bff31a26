package com.hms.authms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
	@Id
	private String id;
	@Indexed(unique = true)
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String role;
	private String referenceId;
	
	public enum Role {
		ADMIN,
		MANAGER,
		USER
	}
}
