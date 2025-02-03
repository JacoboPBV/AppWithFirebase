package com.example.appwithfirebase.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Configurar el DarkMode
        isDarkMode = getSharedPreferences("AppConfig", Context.MODE_PRIVATE)
                .getBoolean("darkMode", false);
        setTheme(isDarkMode ? R.style.ThemeDark : R.style.ThemeLight);

        super.onCreate(savedInstanceState);
        ActivityDashboardBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.changeThemeButton.setImageResource(isDarkMode ? R.drawable.icon_dark_mode : R.drawable.icon_light_mode);

        //Bindings
        binding.logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
        binding.seeFavoritesButton.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, FavoritesActivity.class)));
        binding.changeThemeButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("AppConfig", Context.MODE_PRIVATE).edit();
            editor.putBoolean("darkMode", !isDarkMode);
            editor.apply();

            dashboardViewModel.selectLocation(null); //Arregla problemas entre la Detail y la Dashboard
            recreate();
        });

        //ViewModel
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getLocationLiveData().observe(this, locationList -> {
            if (locationList != null) {
                locationAdapter.setLocations(locationList);
            } else {
                Toast.makeText(this, "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        });
        //Acceder a la DetailActivity de la Location clicada
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

        //Adapter
        locationAdapter = new LocationAdapter(new ArrayList<>(), location ->
                dashboardViewModel.selectLocation(location));

        binding.recyclerViewLocations.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewLocations.setAdapter(locationAdapter);
    }
}
