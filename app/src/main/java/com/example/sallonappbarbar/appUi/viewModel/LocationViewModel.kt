package com.example.sallonappbarbar.appUi.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sallonappbarbar.data.LocationLiveData
import com.example.sallonappbarbar.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(   repo : LocationRepository): ViewModel() {

    private val locationLiveData = LocationLiveData(repo.getLocation())
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }
}