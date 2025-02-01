package com.example.appwithfirebase.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.LocationAdapter;
import com.example.appwithfirebase.databinding.ActivityDashboardBinding;
import com.example.appwithfirebase.viewmodels.DashboardViewModel;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private LocationAdapter locationAdapter;
    private DashboardViewModel dashboardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDashboardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.seeFavoritesButton.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, FavoritesActivity.class)));

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getLocationLiveData().observe(this, locationList -> {
            if (locationList != null) {
                locationAdapter.setLocations(locationList);
            } else {
                Toast.makeText(this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        });

        locationAdapter = new LocationAdapter(new ArrayList<>(), location ->
                dashboardViewModel.selectLocation(location));

        binding.recyclerViewLocations.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewLocations.setAdapter(locationAdapter);

        dashboardViewModel.getSelectedLocation().observe(this, location -> {
            if (location != null) {
                Intent intent = new Intent(DashboardActivity.this, DetailActivity.class);
                intent.putExtra("id", location.getId());
                intent.putExtra("title", location.getTitulo());
                intent.putExtra("description", location.getDescripcion());
                intent.putExtra("image", location.getImagen());
                startActivity(intent);
            }
        });
    }
}
