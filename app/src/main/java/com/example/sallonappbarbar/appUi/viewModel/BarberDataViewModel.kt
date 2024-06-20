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
            Slots(day = "Monday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Tuesday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Wednesday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Thursday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Friday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Saturday", StartTime = "10:00", EndTime = "12:00"),
            Slots(day = "Sunday", StartTime = "10:00", EndTime = "12:00"),
        )
    )

    suspend fun onEvent(event: MainEvent2) {
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


    suspend fun setSlots() {
        viewModelScope.launch { repo.setSlots(openCloseTime.value) }
    }
}

sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent2()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent2()
    data class getServices(val uid: String) : MainEvent2()
    data object setSlots : MainEvent2()

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


