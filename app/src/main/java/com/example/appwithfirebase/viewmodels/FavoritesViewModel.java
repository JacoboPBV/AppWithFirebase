package com.example.appwithfirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.models.Location;
import com.example.appwithfirebase.repositories.FavoritesRepository;

import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private final MutableLiveData<List<Location>> favoriteLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private final FavoritesRepository favoritesRepository;

    public FavoritesViewModel() {
        this.favoritesRepository = new FavoritesRepository();
        loadFavorites();
    }

    public LiveData<List<Location>> getFavoritesLiveData() {
        return favoriteLiveData;
    }

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void selectLocation(Location location) {
        selectedLocation.setValue(location);
    }

    public void loadFavorites() {
        favoritesRepository.fetchFavoriteLocations(favoriteLiveData);
    }
}
