package com.example.appwithfirebase.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.appwithfirebase.models.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import kotlin.collections.ArrayDeque;

public class DashboardRepository {
    private final DatabaseReference dashboardRef;

    public DashboardRepository() {
        this.dashboardRef = FirebaseDatabase.getInstance().getReference("items");
    }

    public void readItemsFromDatabase(MutableLiveData<List<Location>> locationLiveData) {
        dashboardRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Location> locationList = new ArrayDeque<>();
                locationList.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    String id = data.getKey();
                    String title = data.child("title").getValue(String.class);
                    String description = data.child("description").getValue(String.class);
                    String image = data.child("image").getValue(String.class);

                    Location location = new Location(id, title, description, image);
                    locationList.add(location);
                }

                locationLiveData.setValue(locationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer datos", error.toException());
            }
        });
    }
}
