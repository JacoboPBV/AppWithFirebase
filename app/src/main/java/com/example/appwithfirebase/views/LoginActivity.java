package com.example.appwithfirebase.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.repositories.UserRepository;
import com.example.appwithfirebase.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.loginButton).setOnClickListener(v -> loginUser());
        findViewById(R.id.registerButton).setOnClickListener(v -> {
            Intent registerIntent = new Intent(context, RegisterActivity.class);
            context.startActivity(registerIntent);
        });
    }

    private void loginUser() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.login(this, email, password, new UserRepository.OnLoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(LoginActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}