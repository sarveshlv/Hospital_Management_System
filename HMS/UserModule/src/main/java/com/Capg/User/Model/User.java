package com.Capg.User.Model;

import com.Capg.User.Constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String userId;
	@Indexed(unique = true)
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String referenceId;
    private boolean loggedIn;
    
}
