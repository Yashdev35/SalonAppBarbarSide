package com.example.sallonappbarbar.appUi.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.Slots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllBarberInfoViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel(){

    private var _barber = mutableStateOf(BarberModel())
    var barber: State<BarberModel> = _barber

    private val _serviceList = mutableStateOf<List<ServiceCat>>(emptyList())
    val serviceList : State<List<ServiceCat>> = _serviceList

    private val _openCloseTime = mutableStateListOf(
        Slots(day = "Monday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Tuesday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Wednesday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Thursday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Friday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Saturday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Sunday", startTime = "10:00", endTime = "22:00")
    )
    var openCloseTime : SnapshotStateList<Slots> = _openCloseTime

    init {
        viewModelScope.launch {
            getCurrentBarber()
        }
    }
    suspend fun onEvent(event: DisplayBarberEvent) {
        when (event) {
            is DisplayBarberEvent.BarberPersonalInfo -> getCurrentBarber()
            is DisplayBarberEvent.BarberServices -> getServiceList()
            is DisplayBarberEvent.BarberSlots -> getSlots()
        }
    }
    private suspend fun getServiceList(){
        repo.getServices().collect {
            when(it){
                is Resource.Success -> {
                    _serviceList.value = it.result.toMutableList()
                    Log.d("AllldataViewModel", "init: ${_serviceList.value}")
                    getSlots()
                }
                is Resource.Failure -> {
                    Log.d("ServiceViewModel", "init: ${it.exception}")
                }
                is Resource.Loading -> {
                    Log.d("ServiceViewModel", "init: Loading")
                }
            }
        }
    }
    private suspend fun getCurrentBarber() {
        viewModelScope.launch {
            repo.getBarberData().collect {
                when (it) {
                    is Resource.Success -> {
                        _barber.value = it.result
                        getServiceList()
                    }

                    is Resource.Failure -> {
                        _barber.value = BarberModel()
                    }

                    else -> {}
                }
            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private suspend fun getSlots(){
        viewModelScope.launch {
            val newSlot=repo.getTimeSlot()
            for(slot in newSlot)
                _openCloseTime.find { it.day == slot.day }
                    ?.let { slots ->
                        val index = _openCloseTime.indexOf(slots)
                        if (index != -1) {
                            _openCloseTime[index]=slot
                        }
            }
        }
    }
}

sealed class DisplayBarberEvent {
    data object BarberPersonalInfo : DisplayBarberEvent()
    data object BarberServices : DisplayBarberEvent()
    data object BarberSlots : DisplayBarberEvent()
}