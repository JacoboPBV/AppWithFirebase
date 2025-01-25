package com.example.appwithfirebase.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;


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
}
