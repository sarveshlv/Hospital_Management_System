package com.Capg.User.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDTO {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email ID. Please check the format.")
    private String emailId;

    @NotBlank(message = "Password field is required")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#])[a-zA-Z\\d@#]+$", message = "Password must include at least one uppercase letter, one lowercase letter, one digit, and one special symbol (@ or #).")
    private String password;

//    @NotBlank(message = "Confirm Password field is required")
//    private String confirmPassword;
	@Pattern(regexp = "ADMIN|MANAGER|USER", message = "Role must be ADMIN or MANAGER or USER")
    private String role;
}
