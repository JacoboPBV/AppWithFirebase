package com.example.appwithfirebase.repositories;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.appwithfirebase.views.DashboardActivity;
import com.example.appwithfirebase.views.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.appwithfirebase.models.User;

public class UserRepository {
    private final FirebaseAuth mAuth;
    private final DatabaseReference databaseRef;

    public UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public void registerUser(String email, String password, User user, OnRegisterCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addUserToDatabase(user, callback);
                    } else {
                        Log.e("Firebase", "Error", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    private void addUserToDatabase(User user, OnRegisterCallback callback) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            databaseRef.child(uid).setValue(user)
                    .addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(dbTask.getException());
                        }
                    });
        }
    }

    public void loginUser(String email, String password, OnLoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(mAuth.getCurrentUser());
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }


    public interface OnRegisterCallback {
        void onSuccess();

        void onFailure(Exception exception);
    }

    public interface OnLoginCallback {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception exception);
    }
}
