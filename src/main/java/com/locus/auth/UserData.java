package com.locus.auth;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by nrdagar on 11/07/16.
 */

@Entity
@Table(name = "user_data")
public class UserData implements Serializable {


    @Id
    private  Long userId;


    private String login;

    private String password;

    private String enabled;

    private String firstName;

    private String lastName;

    private String email;

    private Date created;

    private Date lastLogin;



    public UserData()
    {

    }

    public UserData(long userId , String login , String password , String enabled ,
                    String firstname , String lastname , Date created ,Date lastlogin  ){

        this.userId = userId;
        this.login = login;
        this.password = password;
        this.enabled = enabled;
        this.firstName = firstname;
        this.lastName = lastname;
        this.created = created;
        this.lastLogin = lastlogin;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
