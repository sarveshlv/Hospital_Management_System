package com.Capg.Authentication.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInDTO {

    @NotBlank(message = "Email ID is required")
    @Email(message = "Invalid email ID")
    private String email;
    private String password;
}
