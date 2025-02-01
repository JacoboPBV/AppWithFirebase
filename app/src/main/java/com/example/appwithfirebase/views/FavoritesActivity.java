package com.example.appwithfirebase.views;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.FavoritesAdapter;
import com.example.appwithfirebase.models.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private List<Location> favoriteLocations = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private String uid;
    private OnDataObtainedCallback callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Obtener uid del usuario autenticado
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = authUser != null ? authUser.getUid() : null;

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(FavoritesActivity.this, 3));

        callback = new OnDataObtainedCallback() {
            @Override
            public void OnSuccess() {
                favoritesAdapter = new FavoritesAdapter(FavoritesActivity.this, favoriteLocations);
                recyclerView.setAdapter(favoritesAdapter);
            }

            @Override
            public void OnFailure(DatabaseError error) {
                Log.e("FAV Data", error.getMessage());
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchFavoriteLocations(callback);
    }

    private void fetchFavoriteLocations(OnDataObtainedCallback callback) {
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance()
                .getReference("users").child(uid).child("favorites");

        userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                favoriteLocations.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String locationId = child.getKey();
                    fetchLocationDetails(locationId, callback);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.OnFailure(error);
            }
        });
    }

    private void fetchLocationDetails(String locationId, OnDataObtainedCallback callback) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child(locationId);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String image = snapshot.child("image").getValue(String.class);

                    Location location = new Location(id, title, description, image);
                    favoriteLocations.add(location);
                }
                callback.OnSuccess();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.OnFailure(error);
            }
        });
    }

    public interface OnDataObtainedCallback {
        void OnSuccess();
        void OnFailure(DatabaseError error);
    }
}

