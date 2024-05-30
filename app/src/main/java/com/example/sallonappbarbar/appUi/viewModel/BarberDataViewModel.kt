package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.data.model.WeekDay
import com.example.sallonappbarbar.data.model.aService
import com.practicecoding.sallonapp.appui.components.CommonDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarberDataViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {

    private val _barberData = MutableStateFlow<Resource<BarberModel>>(Resource.Loading)
    val barberData: StateFlow<Resource<BarberModel>> = _barberData

    private val _barberSlots = MutableStateFlow<Resource<List<WeekDay>>>(Resource.Loading)
    val barberSlots: StateFlow<Resource<List<WeekDay>>> = _barberSlots

    private val _openCloseTime = MutableStateFlow<Resource<String>>(Resource.Loading)
    val openCloseTime: StateFlow<Resource<String>> = _openCloseTime

    fun fetchBarberData() {
        viewModelScope.launch {
            repo.getBarberData().collect {
                _barberData.value = it
            }
        }
    }

    fun fetchBarberSlots() {
        viewModelScope.launch {
            repo.getBarberSlots().collect {
                _barberSlots.value = it
            }
        }
    }

    fun fetchOpenCloseTime() {
        viewModelScope.launch {
            repo.getOpenCloseTime().collect {
                _openCloseTime.value = it
            }
        }
    }

    suspend fun addUserData(barberModel: BarberModel, imageUri: Uri?, activity: Activity) = repo.addUser(barberModel, imageUri)
    suspend fun addServiceData(aServices: List<ServiceType>, activity: Activity) = repo.addServices(aServices)
    suspend fun isShopOpen(shopOpen: Boolean, activity: Activity) = repo.isShopOpen(shopOpen)
    suspend fun addOpenCloseTime(openCloseTime: String, activity: Activity) = repo.addOpenCloseTime(openCloseTime)
    suspend fun addSlots(weekDays: List<WeekDay>, activity: Activity) = repo.addSlots(weekDays)
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


