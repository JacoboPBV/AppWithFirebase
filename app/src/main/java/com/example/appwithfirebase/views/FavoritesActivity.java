package com.example.appwithfirebase.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.LocationAdapter;
import com.example.appwithfirebase.databinding.ActivityFavoritesBinding;
import com.example.appwithfirebase.viewmodels.FavoritesViewModel;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    private LocationAdapter favoritesAdapter;
    private FavoritesViewModel favoritesViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFavoritesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_favorites);

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        favoritesViewModel.getFavoritesLiveData().observe(this, favoritesList -> {
            if (favoritesList != null)
                favoritesAdapter.setLocations(favoritesList);
            else Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
        });

        favoritesAdapter = new LocationAdapter(new ArrayList<>(), location ->
                favoritesViewModel.selectLocation(location));

        binding.recyclerViewFavorites.setLayoutManager(new GridLayoutManager(FavoritesActivity.this, 3));
        binding.recyclerViewFavorites.setAdapter(favoritesAdapter);

        favoritesViewModel.getSelectedLocation().observe(this, location -> {
            if (location != null) {
                Intent intent = new Intent(FavoritesActivity.this, DetailActivity.class);
                intent.putExtra("id", location.getId());
                intent.putExtra("title", location.getTitulo());
                intent.putExtra("description", location.getDescripcion());
                intent.putExtra("image", location.getImagen());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoritesViewModel.loadFavorites();
    }
}

