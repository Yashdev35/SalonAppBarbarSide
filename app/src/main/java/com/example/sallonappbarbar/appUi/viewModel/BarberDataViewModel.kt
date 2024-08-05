package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceCat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {

    suspend fun getBarberData(activity: Activity) = repo.getBarberData()
    suspend fun addUserData(barberModel: BarberModel, imageUri: Uri?, activity: Activity) =
        repo.addBarber(barberModel, imageUri)

    suspend fun addServiceData(aServices: List<ServiceCat>, activity: Activity) =
        repo.addServices(aServices)


}


//sealed class MainEvent {
//    data class getBarberPopular(val city: String, val limit: Long) : BarberEvent()
//    data class getBarberNearby(val city: String, val limit: Long) : BarberEvent()
//    data class getServices(val uid: String) : BarberEvent()
//    data object setSlots : BarberEvent()
//
//}