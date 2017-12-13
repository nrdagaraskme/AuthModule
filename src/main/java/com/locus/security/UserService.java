package com.locus.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 09/12/17.
 */

@Service
public class UserService {

    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(Environment env) {
        this.env = env;
    }



    public User loginuser(String username, String password) {
       List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return new User("Naveen", "", "cf909c43d2154886", roles,"","");
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
class LoginData {
    final int status;
    public final String mobile;
    public final String token;
    public final String userId;
    public final List<String> roles;

    @JsonCreator
    public LoginData(
            @JsonProperty("status") int status,
            @JsonProperty("token") String token,
            @JsonProperty("userId") String userId) {
        this.status = status;
        this.mobile = "";
        this.token = token;
        this.userId = userId;
        this.roles = new ArrayList<>();
        this.roles.add("USER");
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
class UserProfile {

    final UserData user;

    @JsonCreator
    public UserProfile(@JsonProperty("profile") UserData user) {
        this.user = user;
    }
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
class UserData {
    final String userId;
    final String fullName;
    final String mobile;
    final String email;

    @JsonCreator
    public UserData(
            @JsonProperty("userId") String userId,
            @JsonProperty("firstName") String fullName,
            @JsonProperty("userName") String mobile,
            @JsonProperty("email") String email)
             {
        this.userId = userId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
    }
}

