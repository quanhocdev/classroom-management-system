package com.example.classroom.dto.account;


public class LoginResponseDTO {


    private String accessToken;


    private String username;


    private String role;


    private String message;



    public LoginResponseDTO() {
    }



    public LoginResponseDTO(
            String accessToken,
            String username,
            String role,
            String message
    ) {

        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
        this.message = message;

    }



    public String getAccessToken() {
        return accessToken;
    }



    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }



    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }



    public String getRole() {
        return role;
    }



    public void setRole(String role) {
        this.role = role;
    }



    public String getMessage() {
        return message;
    }



    public void setMessage(String message) {
        this.message = message;
    }

}