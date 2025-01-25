package com.example.appwithfirebase.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.repositories.UserRepository;

public class LoginViewModel extends ViewModel {
    private final UserRepository userRepository;

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    public void login(Context context, String email, String password, UserRepository.OnLoginCallback callback){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.loginUser(email, password, callback);
    }
}
