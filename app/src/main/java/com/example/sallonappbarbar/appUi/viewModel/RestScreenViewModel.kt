package com.example.sallonappbarbar.appUi.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sallonappbarbar.appUi.Screens.MainScreens.TimeSlot
import com.example.sallonappbarbar.data.model.aService
import android.os.Parcelable
import com.example.sallonappbarbar.data.model.Service
import com.example.sallonappbarbar.data.model.ServiceCat
import kotlinx.parcelize.Parcelize

class RestScreenViewModel:ViewModel(){
    var genderCounter = mutableStateOf<List<Int>>(value = listOf(0,0,0))
    var listOfService = mutableStateOf<List<Service>>(emptyList())
    var selectedSlots = mutableStateListOf<TimeSlot>()

    fun initializedServices(serviceCat:List<ServiceCat>){
        if (listOfService.value.isEmpty()) {
            val initialServices = serviceCat.flatMap { servicecat ->
                servicecat.services.map { serviceModel ->
                    Service(
                        serviceName = serviceModel.name ?: "",
                        count = 0, // Initialize count to 0
                        price = serviceModel.price,
                        time = serviceModel.time,
                        id = serviceModel.name ?: "",
                        type = servicecat.type ?: ""
                    )
                }
            }
            listOfService.value = initialServices
        }

    }
    fun updateService(updatedService: Service) {
        listOfService.value = listOfService.value.map {
            if (it.id == updatedService.id) updatedService else it
        }
    }
}