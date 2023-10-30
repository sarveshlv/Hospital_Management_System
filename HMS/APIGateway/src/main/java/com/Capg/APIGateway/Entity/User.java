package com.Capg.APIGateway.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String emailId;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String referenceId;
    private boolean loggedIn;

    public enum Role {
        ADMIN,MANAGER,USER
    }
}
