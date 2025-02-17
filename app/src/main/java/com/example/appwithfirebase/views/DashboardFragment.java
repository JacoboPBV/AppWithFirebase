package com.example.appwithfirebase.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appwithfirebase.R;
import com.example.appwithfirebase.adapters.LocationAdapter;
import com.example.appwithfirebase.databinding.FragmentDashboardBinding;
import com.example.appwithfirebase.viewmodels.DashboardViewModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private LocationAdapter locationAdapter;
    private DashboardViewModel dashboardViewModel;
    private boolean isDarkMode;

    public DashboardFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragment con DataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Configurar el modo oscuro
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        view.getContext().setTheme(isDarkMode ? R.style.ThemeDark : R.style.ThemeLight);
        binding.changeThemeButton.setImageResource(isDarkMode ? R.drawable.icon_dark_mode : R.drawable.icon_light_mode);

        // Configurar los click listeners de los botones
        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        binding.menu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        binding.changeThemeButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", !isDarkMode);
            editor.apply();

            requireActivity().recreate();
        });

        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        dashboardViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), locationList -> {
            if (locationList != null) {
                locationAdapter.setLocations(locationList);
            } else {
                Toast.makeText(getContext(), "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el adaptador
        locationAdapter = new LocationAdapter(new ArrayList<>(), location -> {
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

        binding.recyclerViewLocations.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerViewLocations.setAdapter(locationAdapter);
    }
}
