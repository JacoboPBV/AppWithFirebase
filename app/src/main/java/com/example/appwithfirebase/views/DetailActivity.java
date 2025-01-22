package com.example.appwithfirebase.views;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailActivity extends AppCompatActivity {
    TextView titulo;
    TextView descripcion;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titulo = findViewById(R.id.itemTitle);
        descripcion = findViewById(R.id.itemDescription);
        imagen = findViewById(R.id.itemImage);

        String title = getIntent().getStringExtra("title");
        titulo.setText(title);
        String description = getIntent().getStringExtra("description");
        descripcion.setText(description);
        String image = getIntent().getStringExtra("image");
        Glide.with(DetailActivity.this)
                .load(image)
                .into(imagen);
    }

    private void readItemsFromDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child("item");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue(String.class);
                titulo.setText(title);

                String description = dataSnapshot.child("description").getValue(String.class);
                descripcion.setText(description);

                String image = dataSnapshot.child("image").getValue(String.class);
                Glide.with(DetailActivity.this)
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
