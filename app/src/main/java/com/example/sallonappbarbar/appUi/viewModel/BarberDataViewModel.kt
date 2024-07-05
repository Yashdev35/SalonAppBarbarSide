package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.Slots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {



    suspend fun onEvent(event: MainEvent2) {
        when (event) {
            is MainEvent.setSlots -> {}
            else -> {}
        }
    }

    suspend fun getBarberData(activity: Activity) = repo.getBarberData()
    suspend fun addUserData(barberModel: BarberModel, imageUri: Uri?, activity: Activity) =
        repo.addUser(barberModel, imageUri)

    suspend fun addServiceData(aServices: List<ServiceCat>, activity: Activity) =
        repo.addServices(aServices)


}

sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent2()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent2()
    data class getServices(val uid: String) : MainEvent2()
    data object setSlots : MainEvent2()

}