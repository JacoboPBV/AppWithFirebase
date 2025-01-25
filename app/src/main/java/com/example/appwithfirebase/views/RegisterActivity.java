package com.example.appwithfirebase.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.repositories.UserRepository;
import com.example.appwithfirebase.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
        findViewById(R.id.loginButton).setOnClickListener(view -> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private void registerUser() {
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPasswordEditText)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        registerViewModel.register(this, name, email, password, confirmPassword, phoneNumber, address, new UserRepository.OnRegisterCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(RegisterActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
