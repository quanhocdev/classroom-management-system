package com.example.classroom.dto.account;

public class LocalLoginRequestDTO {

    private String username;

    private String password;

    public LocalLoginRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}