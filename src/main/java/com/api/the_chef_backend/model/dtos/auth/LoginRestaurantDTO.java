package com.api.the_chef_backend.model.dtos.auth;


import jakarta.validation.constraints.NotEmpty;

public class LoginRestaurantDTO {

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

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