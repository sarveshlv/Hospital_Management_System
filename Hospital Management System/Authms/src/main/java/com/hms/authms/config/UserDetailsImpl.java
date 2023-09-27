package com.hms.authms.config;

import org.springframework.security.core.userdetails.UserDetails;

import com.hms.authms.entity.User;

import lombok.ToString;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@ToString
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 656958619627944169L;
	
    private String email;
    private String password;
	private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
		this.authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}