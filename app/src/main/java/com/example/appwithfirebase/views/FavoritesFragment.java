package com.example.appwithfirebase.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.LocationAdapter;
import com.example.appwithfirebase.databinding.FragmentFavoritesBinding;
import com.example.appwithfirebase.viewmodels.FavoritesViewModel;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private LocationAdapter favoritesAdapter;
    private FavoritesViewModel favoritesViewModel;

    public FavoritesFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Infla el layout para el fragment usando DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView y Adapter
        favoritesAdapter = new LocationAdapter(new ArrayList<>(), location ->
                favoritesViewModel.selectLocation(location));
        binding.recyclerViewFavorites.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerViewFavorites.setAdapter(favoritesAdapter);

        // Inicializar el ViewModel
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        // Observar la lista de favoritos
        favoritesViewModel.getFavoritesLiveData().observe(getViewLifecycleOwner(), favoritesList -> {
            if (favoritesList != null) {
                favoritesAdapter.setLocations(favoritesList);
            } else {
                Toast.makeText(getContext(), "Failed to load favorites", Toast.LENGTH_SHORT).show();
            }
        });

        // Observar la ubicación seleccionada para abrir DetailActivity
        favoritesViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", location.getId());
                intent.putExtra("title", location.getTitulo());
                intent.putExtra("description", location.getDescripcion());
                intent.putExtra("image", location.getImagen());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar los favoritos cada vez que el fragment vuelva a primer plano
        favoritesViewModel.loadFavorites();
    }
}
