package com.Capg.Authentication.Model;

import lombok.Data;
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
    private Role role;
    private String referenceId;
    private Boolean loggedIn;

    public enum Role {
        ADMIN, MANAGER, USER
    }

}
