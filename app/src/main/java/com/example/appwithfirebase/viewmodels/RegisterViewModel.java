package com.example.appwithfirebase.viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.appwithfirebase.models.User;
import com.example.appwithfirebase.repositories.UserRepository;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;

    public RegisterViewModel() {
        userRepository = new UserRepository();
    }

    public void register(String email, String password, User user, UserRepository.OnRegisterCallback callback) {
        userRepository.registerUser(email, password, user, callback);
    }
}
