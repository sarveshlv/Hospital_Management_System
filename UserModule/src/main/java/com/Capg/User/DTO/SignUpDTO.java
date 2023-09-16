package com.Capg.User.DTO;

import java.util.Objects;

import com.Capg.User.Constants.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class SignUpDTO {

    @NotBlank(message = "Email field is required")
    @Email(message = "Invalid email ID. Please check the format.")
    private String emailId;

    @NotBlank(message = "Password field is required")
    @Size(min = 8, message = "Password must be min of 8 characters")
    private String password;

    @NotBlank(message = "Confirm Password field is required")
    private String confirmPassword;

    private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public SignUpDTO(
			@NotBlank(message = "Email field is required") @Email(message = "Invalid email ID. Please check the format.") String emailId,
			@NotBlank(message = "Password field is required") @Size(min = 8, message = "Password must be min of 8 characters") String password,
			@NotBlank(message = "Confirm Password field is required") String confirmPassword, Role role) {
		super();
		this.emailId = emailId;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.role = role;
	}

	@Override
	public String toString() {
		return "SignUpDTO [emailId=" + emailId + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ ", role=" + role + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(confirmPassword, emailId, password, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignUpDTO other = (SignUpDTO) obj;
		return Objects.equals(confirmPassword, other.confirmPassword) && Objects.equals(emailId, other.emailId)
				&& Objects.equals(password, other.password) && role == other.role;
	}

	public SignUpDTO() {
		super();
	}
	
	

}
