package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.Slots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {

    var openCloseTime = mutableStateOf(
        listOf(
            Slots("Monday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Tuesday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Wednesday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Thrusday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Friday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Saturday", StartTime = "10:00", EndTime = "12:00"),
            Slots("Sunday", StartTime = "10:00", EndTime = "12:00"),
        )
    )

    suspend fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.setSlots -> setSlots()
            else -> {}
        }
    }

    suspend fun getBarberData(activity: Activity) = repo.getBarberData()
    suspend fun addUserData(barberModel: BarberModel, imageUri: Uri?, activity: Activity) =
        repo.addUser(barberModel, imageUri)

    suspend fun addServiceData(aServices: List<ServiceType>, activity: Activity) =
        repo.addServices(aServices)

    suspend fun isShopOpen(shopOpen: Boolean, activity: Activity) = repo.isShopOpen(shopOpen)
    suspend fun addOpenCloseTime(openCloseTime: String, activity: Activity) =
        repo.addOpenCloseTime(openCloseTime)

    suspend fun setSlots() {
        viewModelScope.launch { repo.setSlots(openCloseTime.value) }
    }

//    suspend fun retrieveSlots(activity: Activity) = repo.retrieveSlots()
}

sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent()
    data class getServices(val uid: String) : MainEvent()
    data object setSlots : MainEvent()

}


//viewModel.getBarberSlots(activity).collect { resource ->
//    when (resource) {
//        is Resource.Success -> {
//            isLoading = false
//            val slotsData = resource.result
//            if (slotsData.isNotEmpty()) {
//                slots.clear()
//                slots.addAll(slotsData[0].slots)
//                Toast.makeText(activity, "Slots Loaded", Toast.LENGTH_LONG).show()
//                viewModel.getOpenCloseTime(activity).collect { resource ->
//                    when (resource) {
//                        is Resource.Success -> {
//                            isLoading = false
//                            Toast.makeText(activity, "Time Loaded", Toast.LENGTH_SHORT).show()
//                            val openCloseTime = resource.result
//                            val time = openCloseTime.split(" - ")
//                            if (time.size == 2) {
//                                openTime = time[0]
//                                closeTime = time[1]
//                            } else {
//                                openTime = "N/A"
//                                closeTime = "N/A"
//                            }
//                        }
//                        is Resource.Failure -> {
//                            isLoading = false
//                            // Handle data fetching error here (e.g., show a toast)
//                            Toast.makeText(
//                                activity,
//                                "Error fetching data: ${resource.exception.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        is Resource.Loading -> {
//                            isLoading = true
//                        }
//                    }
//                }
//            } else {
//                Toast.makeText(activity, "No slots available", Toast.LENGTH_LONG).show()
//            }
//        }
//        is Resource.Failure -> {
//            isLoading = false
//            Toast.makeText(activity, "Error fetching data: ${resource.exception.message}", Toast.LENGTH_SHORT).show()
//        }
//        is Resource.Loading -> {
//            isLoading = true
//        }
//    }
//}


