package com.kilanov.userswebservice.ui.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginRequest {
    @NotNull(message = "Email can not be null")
    @Email
    private String email;
    @NotNull(message = "Password can not be null")
    @Size(min = 8, max = 16,message = "Password should be less than 16 characters")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
