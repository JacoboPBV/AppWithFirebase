package com.example.appwithfirebase.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.example.appwithfirebase.databinding.ItemLocationBinding;
import com.example.appwithfirebase.models.Location;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<Location> locationList;
    private final OnLocationClickListener listener;

    public LocationAdapter(List<Location> locationList, OnLocationClickListener listener) {
        this.locationList = locationList;
        this.listener = listener;
    }

    public void setLocations(List<Location> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLocationBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_location,
                parent,
                false
        );

        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationList.get(position);

        holder.bind(location, listener);
    }

    @Override
    public int getItemCount() {
        return locationList != null ? locationList.size() : 0;
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final ItemLocationBinding binding;

        public LocationViewHolder(@NonNull ItemLocationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Location location, OnLocationClickListener listener) {
            binding.setLocation(location);
            Glide.with(binding.getRoot().getContext())
                    .load(binding.getLocation().getImagen())
                    .into(binding.imageViewLocation);


            this.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLocationClick(location);
                }
            });

            binding.executePendingBindings();
        }
    }

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }
}
