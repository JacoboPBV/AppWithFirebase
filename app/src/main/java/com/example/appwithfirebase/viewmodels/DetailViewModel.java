package com.example.appwithfirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.repositories.FavoritesRepository;

public class DetailViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final FavoritesRepository favoritesRepository;

    public DetailViewModel() {
        this.favoritesRepository = new FavoritesRepository();
    }

    public void setLocationID(String locationID) {
        favoritesRepository.checkIfFavorite(isFavorite, locationID);
    }

    public LiveData<Boolean> isFavorite() {
        return isFavorite;
    }

    public void markAsFavorite(String locationID) {
        favoritesRepository.toggleFavorite(isFavorite, locationID);
    }
}
