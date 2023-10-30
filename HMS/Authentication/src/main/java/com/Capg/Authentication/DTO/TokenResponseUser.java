package com.Capg.Authentication.DTO;

import lombok.Data;

@Data
public class TokenResponseUser {

    private String type = "Bearer";
    private String token;
    private UserDetails userDetails;

    public TokenResponseUser(String token, UserDetails userDetails) {
        this.token = token;
        this.userDetails = userDetails;
    }
}
