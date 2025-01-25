package com.example.appwithfirebase.viewmodels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.models.User;
import com.example.appwithfirebase.repositories.UserRepository;

import java.util.stream.Stream;

public class RegisterViewModel extends ViewModel {
    private final UserRepository userRepository;

    public RegisterViewModel() {
        userRepository = new UserRepository();
    }

    public void register(Context context, String name, String email, String password, String confirmPassword, String phoneNumber, String address, UserRepository.OnRegisterCallback callback) {
        if (Stream.of(name, email, password, confirmPassword, phoneNumber, address).anyMatch(String::isEmpty)) {
            Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(context, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, phoneNumber, address);
        userRepository.registerUser(email, password, user, callback);
    }
}
