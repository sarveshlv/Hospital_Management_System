package com.Capg.User.DTO;

import lombok.Data;

@Data
public class UserDetails {

    private String userId;
    private String firstName;
    private String lastName;
    private String role;
    private String emailId;
    private String referenceId;
    private boolean loggedIn;
}
