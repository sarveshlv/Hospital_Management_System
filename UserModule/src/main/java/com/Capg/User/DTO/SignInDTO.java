package com.Capg.User.DTO;

import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class SignInDTO {

    @NotEmpty(message = "Email field is required")
    @Email(message = "Invalid email ID")
    private String emailId;

    @NotEmpty(message = "Password field is required")
    private String password;

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

	public SignInDTO(@NotEmpty(message = "Email field is required") @Email(message = "Invalid email ID") String emailId,
			@NotEmpty(message = "Password field is required") String password) {
		super();
		this.emailId = emailId;
		this.password = password;
	}

	@Override
	public String toString() {
		return "SignInDTO [emailId=" + emailId + ", password=" + password + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailId, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignInDTO other = (SignInDTO) obj;
		return Objects.equals(emailId, other.emailId) && Objects.equals(password, other.password);
	}

	public SignInDTO() {
		super();
	}
    
    
}
