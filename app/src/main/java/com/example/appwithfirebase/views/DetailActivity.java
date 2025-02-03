package com.example.appwithfirebase.views;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.example.appwithfirebase.databinding.ActivityDetailBinding;
import com.example.appwithfirebase.viewmodels.DetailViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    DetailViewModel detailViewModel;
    DatabaseReference userFavoritesRef;
    String uid;
    String locationID;
    boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isDarkMode = getSharedPreferences("AppConfig", Context.MODE_PRIVATE)
                .getBoolean("darkMode", false);
        setTheme(isDarkMode ? R.style.ThemeDark : R.style.ThemeLight);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        //Item stats
        locationID = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        binding.itemTitle.setText(title);
        String description = getIntent().getStringExtra("description");
        binding.itemDescription.setText(description);
        String image = getIntent().getStringExtra("image");
        Glide.with(DetailActivity.this)
                .load(image)
                .into(binding.itemImage);

        // Obtener uid del usuario autenticado
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = authUser != null ? authUser.getUid() : null;

        //Si el usuario se ha loggeado, inicia los favoritos
        if (uid != null) {
            detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
            detailViewModel.setLocationID(locationID);

            //Favoritos
            binding.markFavoriteButton.setOnClickListener(v -> {
                detailViewModel.markAsFavorite(locationID);
                binding.markFavoriteButton.setImageResource(Boolean.TRUE.equals(detailViewModel.isFavorite().getValue()) ?
                        R.drawable.icon_favorite_enabled : R.drawable.icon_favorite_disabled);
                Toast.makeText(this, Boolean.TRUE.equals(detailViewModel.isFavorite().getValue()) ?
                        "Marcado como favorito" : "Desmarcado de favoritos", Toast.LENGTH_SHORT).show();
            });

            userFavoritesRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("favorites");

            detailViewModel.isFavorite().observe(this, favorite ->
                    binding.markFavoriteButton.setImageResource(Boolean.TRUE.equals(detailViewModel.isFavorite().getValue()) ?
                            R.drawable.icon_favorite_enabled : R.drawable.icon_favorite_disabled
                    ));
        } else {
            Toast.makeText(this, "No se ha podido establecer conexion con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }
}
