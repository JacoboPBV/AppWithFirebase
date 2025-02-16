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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
        // Configurar el modo oscuro (el tema en sí se aplica a nivel de Activity)
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = sharedPreferences.getBoolean("darkMode", false);
        binding.changeThemeButton.setImageResource(isDarkMode ? R.drawable.icon_dark_mode : R.drawable.icon_light_mode);

        // Configurar los click listeners de los botones
        binding.logoutButton.setOnClickListener(v -> {
            // Cerrar sesión y regresar a LoginActivity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });
        binding.changeThemeButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", !isDarkMode);
            editor.apply();

            // Reinicia la Activity para aplicar el nuevo tema
            dashboardViewModel.selectLocation(null); // Evita conflictos entre Detail y Dashboard
            requireActivity().recreate();
        });

        // Inicializar el ViewModel (usamos el scope de la Activity para compartirlo entre fragments, si se requiere)
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        dashboardViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), locationList -> {
            if (locationList != null) {
                locationAdapter.setLocations(locationList);
            } else {
                Toast.makeText(getContext(), "Failed to load locations", Toast.LENGTH_SHORT).show();
            }
        });
        // Navegar a DetailActivity cuando se seleccione una Location
        dashboardViewModel.getSelectedLocation().observe(getViewLifecycleOwner(), location -> {
            if (location != null) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", location.getId());
                intent.putExtra("title", location.getTitulo());
                intent.putExtra("description", location.getDescripcion());
                intent.putExtra("image", location.getImagen());
                startActivity(intent);
            }
        });

        // Configurar el adaptador
        locationAdapter = new LocationAdapter(new ArrayList<>(), location ->
                dashboardViewModel.selectLocation(location));
        binding.recyclerViewLocations.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.recyclerViewLocations.setAdapter(locationAdapter);
    }
}
