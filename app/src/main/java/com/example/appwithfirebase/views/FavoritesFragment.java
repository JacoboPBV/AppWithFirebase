package com.example.appwithfirebase.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
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

        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        binding.menu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Configurar RecyclerView y Adapter
        favoritesAdapter = new LocationAdapter(new ArrayList<>(), location -> {
            if (location != null) {
                DetailFragment detailFragment = new DetailFragment();

                Bundle bundle = new Bundle();
                bundle.putString("id", location.getId());
                bundle.putString("title", location.getTitulo());
                bundle.putString("description", location.getDescripcion());
                bundle.putString("image", location.getImagen());
                detailFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, detailFragment)
                        .addToBackStack(null) // Permite volver con el botón "atrás"
                        .commit();
            }
        });
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
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar los favoritos cada vez que el fragment vuelva a primer plano
        favoritesViewModel.loadFavorites();
    }
}
