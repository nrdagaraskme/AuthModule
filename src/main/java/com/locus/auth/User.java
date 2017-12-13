package com.locus.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naveen on 09/12/17.
 */
public class User implements UserDetails {

    private final String username;
    private final String fullName;
    private final String mobile;
    private final String email;
    private final String password;
    private final String userID;
    private String userToken;
    private final boolean active;
    private final boolean locked;
    private final Set<GrantedAuthority> authorities;

    public User(String username, String password, String userID, List<GrantedAuthority> roles, String fullName , String userToken ) {
        this.password = password;
        this.username = username;
        this.userID = userID;
        this.userToken = userToken;
        this.fullName = fullName;
        this.authorities = new HashSet<>();
        for(GrantedAuthority role: roles) {
            this.authorities.add(role);
        }
        this.active = true;
        this.locked = false;
        this.email = "";
        this.mobile = "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.locked;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserID() {
        return userID;
    }

    @Override

    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    public String getFullName() {
        return fullName;
    }
}
