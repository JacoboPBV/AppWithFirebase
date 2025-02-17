package com.example.appwithfirebase.views;

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

import com.bumptech.glide.Glide;
import com.example.appwithfirebase.R;
import com.example.appwithfirebase.databinding.FragmentDetailBinding;
import com.example.appwithfirebase.viewmodels.DetailViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private DetailViewModel detailViewModel;
    private DatabaseReference userFavoritesRef;
    private String uid;
    private String locationID;

    public DetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Recuperar argumentos (asegúrate de que se pasen correctamente)
        Bundle args = getArguments();
        if (args != null) {
            locationID = args.getString("id");
            String title = args.getString("title");
            String description = args.getString("description");
            String image = args.getString("image");

            binding.itemTitle.setText(title);
            binding.itemDescription.setText(description);
            Glide.with(requireContext())
                    .load(image)
                    .into(binding.itemImage);
        }

        // Obtener uid del usuario autenticado
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = authUser != null ? authUser.getUid() : null;

        if (uid != null) {
            // Inicializar el ViewModel (puedes usar el scope del fragment o de la activity según tus necesidades)
            detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
            detailViewModel.setLocationID(locationID);

            // Configurar el click del botón de favoritos
            binding.markFavoriteButton.setOnClickListener(v -> {
                detailViewModel.markAsFavorite(locationID);
                boolean isFav = Boolean.TRUE.equals(detailViewModel.isFavorite().getValue());
                binding.markFavoriteButton.setImageResource(isFav ?
                        R.drawable.icon_favorite_enabled : R.drawable.icon_favorite_disabled);
                Toast.makeText(requireContext(), isFav ?
                        "Marcado como favorito" : "Desmarcado de favoritos", Toast.LENGTH_SHORT).show();
            });

            userFavoritesRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("favorites");

            // Observar el estado de favorito y actualizar el botón
            detailViewModel.isFavorite().observe(getViewLifecycleOwner(), favorite ->
                    binding.markFavoriteButton.setImageResource(Boolean.TRUE.equals(favorite) ?
                            R.drawable.icon_favorite_enabled : R.drawable.icon_favorite_disabled));
        } else {
            Toast.makeText(requireContext(), "No se ha podido establecer conexión con la base de datos", Toast.LENGTH_SHORT).show();
        }
    }
}
