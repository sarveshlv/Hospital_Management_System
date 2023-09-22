package com.Capg.User.Model;

import com.Capg.User.Constants.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String emailId;
    private String password;
    private Role role;
    private String referenceId;

    public User(String emailId, String password, Role role) {
        this.emailId = emailId;
        this.password = password;
        this.role = role;
		this.referenceId = null;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailId, password, referenceId, role, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(emailId, other.emailId) && Objects.equals(password, other.password)
				&& Objects.equals(referenceId, other.referenceId) && role == other.role
				&& Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailId=" + emailId + ", password=" + password + ", role=" + role
				+ ", referenceId=" + referenceId + "]";
	}
    
    
}
