// package com.example.classroom.security;

// import org.springframework.stereotype.Service;

// import com.google.firebase.auth.FirebaseAuth;
// import com.google.firebase.auth.FirebaseToken;

// @Service
// public class FirebaseService {


//     public FirebaseToken verifyToken(String idToken) {

//         try {

//             return FirebaseAuth
//                     .getInstance()
//                     .verifyIdToken(idToken);

//         } catch (Exception e) {

//             throw new RuntimeException(
//                     "Firebase token không hợp lệ"
//             );
//         }
//     }

// }