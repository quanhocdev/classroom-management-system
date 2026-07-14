package com.example.classroom.dto.account;

public class GoogleLoginRequestDTO {

    private String firebaseIdToken;

    public GoogleLoginRequestDTO() {
    }

    public String getFirebaseIdToken() {
        return firebaseIdToken;
    }

    public void setFirebaseIdToken(String firebaseIdToken) {
        this.firebaseIdToken = firebaseIdToken;
    }
}