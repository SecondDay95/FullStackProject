package com.example.demo.exception;

public class UsernameNotFoundResponse {

    private String username;

    public UsernameNotFoundResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
