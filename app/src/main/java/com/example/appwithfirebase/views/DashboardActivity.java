package com.example.appwithfirebase.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.LocationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.example.appwithfirebase.models.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    Context context = this;
    private FirebaseAuth mAuth;
    TextView titulo;
    TextView descripcion;
    ImageView imagen;
    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    private List<Location> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.logoutButton).setOnClickListener(view -> {
            mAuth.signOut();

            Intent logoutIntent = new Intent(DashboardActivity.this, LoginActivity.class);
            context.startActivity(logoutIntent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerViewLocations);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        locationAdapter = new LocationAdapter(this, locationList, location -> {
            Intent intent = new Intent(DashboardActivity.this, DetailActivity.class);
            intent.putExtra("title", location.getTitulo());
            intent.putExtra("description", location.getDescripcion());
            intent.putExtra("image", location.getImagen());
            startActivity(intent);
        });
        recyclerView.setAdapter(locationAdapter);


        readItemsFromDatabase();
    }

    private void readItemsFromDatabase() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String title = snapshot.child("title").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);

                    Location location = new Location(title, description, image);
                    locationList.add(location);
                }

                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Firebase", "Error al leer datos", error.toException());
            }
        };

        databaseRef.addListenerForSingleValueEvent(userListener);
    }
}
