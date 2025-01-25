package com.example.appwithfirebase.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appwithfirebase.models.Location;
import com.example.appwithfirebase.repositories.DashboardRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<List<Location>> locationLiveData = new MutableLiveData<>();
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private final DashboardRepository dashboardRepository;

    public DashboardViewModel() {
        dashboardRepository = new DashboardRepository();
        loadLocations();
    }

    public LiveData<List<Location>> getLocationLiveData() {
        return locationLiveData;
    }

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void selectLocation(Location location) {
        selectedLocation.setValue(location);
    }

    private void loadLocations() {
        dashboardRepository.readItemsFromDatabase(locationLiveData);
    }
}
