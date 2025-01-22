package com.example.appwithfirebase.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appwithfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    Context context = this;
    private FirebaseAuth mAuth;
    TextView titulo;
    TextView descripcion;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logoutButton).setOnClickListener(view -> {
            mAuth.signOut();

            Intent logoutIntent= new Intent(DashboardActivity.this, LoginActivity.class);
            context.startActivity(logoutIntent);
            finish();
        });
        findViewById(R.id.moreButton).setOnClickListener(view -> readItemsFromDatabase());

        titulo = findViewById(R.id.itemTitle);
        descripcion = findViewById(R.id.itemDescription);
        imagen = findViewById(R.id.itemImage);

        readItemsFromDatabase();
    }

    private void readItemsFromDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child("item" + (new Random().nextInt(6) + 1));

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue(String.class);
                titulo.setText(title);

                String description = dataSnapshot.child("description").getValue(String.class);
                descripcion.setText(description);

                String image = dataSnapshot.child("image").getValue(String.class);
                Glide.with(DashboardActivity.this)
                        .load(image)
                        .into(imagen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Error al leer datos", databaseError.toException());
            }
        };

        databaseRef.addListenerForSingleValueEvent(userListener);
    }
}
