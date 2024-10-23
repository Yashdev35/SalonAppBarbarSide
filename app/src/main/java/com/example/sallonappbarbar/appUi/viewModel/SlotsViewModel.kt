package com.example.sallonappbarbar.appUi.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.SlotsDay
import com.example.sallonappbarbar.data.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {

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

    var isLoading = mutableStateOf(false)
    var isSuccessfulDialog = mutableStateOf(false)

    var selectedSlots = mutableStateListOf<TimeSlot>()

    suspend fun onEvent(event: SlotsEvent) {
        when (event) {
            is SlotsEvent.SetSlots -> setSlots(event.navController, event.context)
            is SlotsEvent.GetSlots -> getSlots()
            is SlotsEvent.updateSlotTimes -> updateSlotTimes(event.day,event.newStartTime,event.newEndTime,event.context)
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

    private suspend fun setSlots(navController: NavController, context: Context) {
        viewModelScope.launch {
            repo.setSlots(_openCloseTime).collect {
                onComplete(it, navController, context)
            }
        }
    }
    private suspend fun updateSlotTimes(day: String, newStartTime: String, newEndTime: String, context: Context) {
        viewModelScope.launch {
            repo.updateSlotTimes(day, newStartTime, newEndTime).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Slot updated successfully", Toast.LENGTH_SHORT).show()
                        // Update local state
                        val index = _openCloseTime.indexOfFirst { it.day == day }
                        if (index != -1) {
                            _openCloseTime[index] = _openCloseTime[index].copy(startTime = newStartTime, endTime = newEndTime)
                        }
                    }
                    is Resource.Failure -> {
                        Toast.makeText(context, "Failed to update slot: ${resource.exception.message}", Toast.LENGTH_SHORT).show()
                        Log.d("SlotsViewModel", "Failed to update slot: ${resource.exception.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onComplete(it: Resource<String>, navController: NavController, context: Context) {
        when (it) {
            is Resource.Loading -> {
                isLoading.value = true
            }
            is Resource.Success -> {
                isLoading.value = false
                navController.navigate(Screens.Home.route)
            }
            is Resource.Failure -> {
                isLoading.value = false
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

sealed class SlotsEvent {
    data class SetSlots(val navController: NavController, val context: Context) : SlotsEvent()
    data object GetSlots : SlotsEvent()
    data class updateSlotTimes(val day: String,val newStartTime: String,val newEndTime: String,val context: Context) : SlotsEvent()
}