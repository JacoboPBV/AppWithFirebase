package com.example.appwithfirebase;

public class User {
    public String uid;
    public String name;
    public String email;

    public User() {
        // Constructor vacío necesario para Firebase
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
