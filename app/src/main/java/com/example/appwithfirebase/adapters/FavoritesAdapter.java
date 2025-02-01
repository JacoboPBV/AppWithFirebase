package com.example.appwithfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.example.appwithfirebase.models.Location;
import com.example.appwithfirebase.views.DetailActivity;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private final Context context;
    private final List<Location> favoriteLocations;

    public FavoritesAdapter(Context context, List<Location> favoriteLocations) {
        this.context = context;
        this.favoriteLocations = favoriteLocations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = favoriteLocations.get(position);
        holder.title.setText(location.getTitulo());
        Glide.with(context)
                .load(location.getImagen())
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", location.getId());
                intent.putExtra("title", location.getTitulo());
                intent.putExtra("description", location.getDescripcion());
                intent.putExtra("image", location.getImagen());
                context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteLocations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewLocation);
            image = itemView.findViewById(R.id.imageViewLocation);
        }
    }
}
