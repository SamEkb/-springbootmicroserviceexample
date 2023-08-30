package com.kilanov.userswebservice.ui.response;

import java.io.Serial;
import java.io.Serializable;

public class UserCreateResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1443323526075891401L;
    private String firstName;
    private String lastName;
    private String email;
    private String userId;

    public UserCreateResponse() {
    }

    public UserCreateResponse(String firstName, String lastName, String email, String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
