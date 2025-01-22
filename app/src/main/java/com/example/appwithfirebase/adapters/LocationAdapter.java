package com.example.appwithfirebase.adapters;

import android.content.Context;
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
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private final List<Location> locationList;
    private final Context context;
    private final OnLocationClickListener listener;

    public LocationAdapter(Context context, List<Location> locationList, OnLocationClickListener listener) {
        this.context = context;
        this.locationList = locationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);

        Glide.with(context)
                .load(location.getImagen())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(view -> listener.onLocationClick(location));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewLocation);
        }
    }

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }
}
