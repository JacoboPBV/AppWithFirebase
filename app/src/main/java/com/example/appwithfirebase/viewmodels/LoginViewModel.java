package com.example.appwithfirebase.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;

    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(String email, String password, UserRepository.OnLoginCallback callback){
        userRepository.loginUser(email, password, callback);
    }
}
