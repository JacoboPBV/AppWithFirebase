package com.example.appwithfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.stream.Stream;

public class RegisterActivity extends AppCompatActivity {
    Context context = this;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
        findViewById(R.id.loginButton).setOnClickListener(view -> {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            context.startActivity(loginIntent);
        });
    }

    private void registerUser() {
        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String confirmPassword = ((EditText) findViewById(R.id.confirmPasswordEditText)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();

        if (Stream.of(name, email, password, confirmPassword, phoneNumber, address).anyMatch(String::isEmpty)) {
            Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();

                        addUserToDatabase(name, phoneNumber, address);

                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        context.startActivity(loginIntent);
                    } else {
                        Log.e("Firebase", "Error", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToDatabase(String name, String phoneNumber, String address) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            User newUser = new User(name, phoneNumber, address);
            databaseRef.child(uid).setValue(newUser)
                    .addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Log.d("Firebase", "Usuario añadido a Realtime Database con UID: " + uid);
                        } else {
                            Log.e("Firebase", "Error al añadir usuario a la base de datos", dbTask.getException());
                        }
                    });
        }
    }
}
