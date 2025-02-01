package com.example.appwithfirebase.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailActivity extends AppCompatActivity {
    TextView titulo;
    TextView descripcion;
    ImageView imagen;
    DatabaseReference userFavoritesRef;
    FloatingActionButton markFavoriteButton;
    String uid;
    String locationID;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Obtener uid del usuario autenticado
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = authUser != null ? authUser.getUid() : null;

        //Item stats
        titulo = findViewById(R.id.itemTitle);
        descripcion = findViewById(R.id.itemDescription);
        imagen = findViewById(R.id.itemImage);

        locationID = getIntent().getStringExtra("id");
        String title = getIntent().getStringExtra("title");
        titulo.setText(title);
        String description = getIntent().getStringExtra("description");
        descripcion.setText(description);
        String image = getIntent().getStringExtra("image");
        Glide.with(DetailActivity.this)
                .load(image)
                .into(imagen);

        //Favoritos
        markFavoriteButton = findViewById(R.id.markFavoriteButton);
        markFavoriteButton.setOnClickListener(v -> {
            toggleFavorite();
        });

        //Iniciar estado desde la base de datos
        if (uid != null) {
            userFavoritesRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("favorites");

            checkIfFavorite();
        } else {
            Toast.makeText(this, "No se ha podido establecer conexion con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleFavorite() {
        if (!isFavorite) {
            // Marcar como favorito
            userFavoritesRef.child(locationID).setValue(true);
            markFavoriteButton.setImageResource(R.drawable.icon_favorite_enabled);
            Toast.makeText(this, "Marcado como favorito", Toast.LENGTH_SHORT).show();
        } else {
            // Quitar de favoritos
            userFavoritesRef.child(locationID).removeValue();
            markFavoriteButton.setImageResource(R.drawable.icon_favorite_disabled);
            Toast.makeText(this, "Desmarcado de favoritos", Toast.LENGTH_SHORT).show();
        }
        isFavorite = !isFavorite;
    }

    private void checkIfFavorite() {
        userFavoritesRef.child(locationID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isFavorite = snapshot.exists();
                if (isFavorite) {
                    markFavoriteButton.setImageResource(R.drawable.icon_favorite_enabled);
                } else {
                    markFavoriteButton.setImageResource(R.drawable.icon_favorite_disabled);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FAV", error.getMessage());
            }
        });
    }
}
