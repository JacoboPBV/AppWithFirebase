package com.example.appwithfirebase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
        findViewById(R.id.loginButton).setOnClickListener(v -> loginUser());
    }

    private void registerUser() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, ingrese un correo y contraseña válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Firebase", "Error", task.getException());
                        Toast.makeText(MainActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUser() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, ingrese un correo y contraseña válidos.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Error en autenticación.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void readUsersFromDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.child("name").getValue(String.class);
                    Log.d("Firebase", "Nombre del usuario: " + userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Error al leer datos", databaseError.toException());
            }
        };

        databaseRef.addListenerForSingleValueEvent(userListener);
    }

    private void addUserToDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            User newUser = new User(uid, "John Doe", "johndoe@example.com");  // Usa datos reales aquí
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

    private void modifyOrderFromDatabase() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders/order2");

        Map<String, Object> updates = new HashMap<>();
        updates.put("price", 1500);
        updates.put("product", "Laptop Pro 2");

        orderRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Orden actualizada correctamente.");
            } else {
                Log.w("Firebase", "Error al actualizar la orden.", task.getException());
            }
        });
    }

    private void deleteOrderFromDatabase(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders/order2");

        orderRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Orden eliminada correctamente.");
            } else {
                Log.w("Firebase", "Error al eliminar la orden.", task.getException());
            }
        });
    }
}