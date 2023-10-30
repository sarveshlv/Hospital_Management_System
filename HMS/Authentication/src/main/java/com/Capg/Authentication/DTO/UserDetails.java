package com.Capg.Authentication.DTO;

import lombok.Data;

@Data
public class UserDetails {
    private String userId;
    private String emailId;
    private String firstName;
    private String lastName;
    private String role;
    private String referenceId;
    private boolean loggedIn;
}
