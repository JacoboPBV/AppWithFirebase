package com.example.appwithfirebase;

public class User {
    public String name;
    public String phoneNumber;
    public String address;

    public User() {
        // Constructor vacío necesario para Firebase
    }

    public User(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
