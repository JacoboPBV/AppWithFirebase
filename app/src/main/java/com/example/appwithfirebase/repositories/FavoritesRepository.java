package com.example.appwithfirebase.repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.appwithfirebase.models.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import kotlin.collections.ArrayDeque;

public class FavoritesRepository {
    DatabaseReference userFavoritesRef;

    public FavoritesRepository() {
        userFavoritesRef = FirebaseDatabase.getInstance()
                .getReference("users").child(Objects.requireNonNull(FirebaseAuth
                        .getInstance().getCurrentUser()).getUid()).child("favorites");
    }

    public void fetchFavoriteLocations(MutableLiveData<List<Location>> favoritesLiveData) {
        userFavoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Location> favoriteLocations = new ArrayDeque<>();
                favoriteLocations.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String locationId = child.getKey();
                    fetchLocationDetails(favoritesLiveData, locationId, favoriteLocations);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error al leer favoritos", error.toException());
            }
        });
    }

    public void fetchLocationDetails(MutableLiveData<List<Location>> favoritesLiveData, String locationId, List<Location> favoriteLocations) {
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

                favoritesLiveData.setValue(favoriteLocations);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error al leer datos", error.toException());
            }
        });
    }

    public void toggleFavorite(MutableLiveData<Boolean> isFavorite, String locationID) {
        if (Boolean.FALSE.equals(isFavorite.getValue()))
            userFavoritesRef.child(locationID).setValue(true);
        else userFavoritesRef.child(locationID).removeValue();

        isFavorite.setValue(Boolean.FALSE.equals(isFavorite.getValue()));
    }

    public void checkIfFavorite(MutableLiveData<Boolean> isFavorite, String locationID) {
        userFavoritesRef.child(locationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isFavorite.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FAV", error.getMessage());
            }
        });
    }
}
