package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.SlotsDay
import com.example.sallonappbarbar.data.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {


    val openCloseTime = mutableStateListOf(// setting the slots for only open and close time screen
        Slots(day = "Monday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Tuesday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Wednesday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Thursday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Friday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Saturday", startTime = "10:00", endTime = "22:00"),
        Slots(day = "Sunday", startTime = "10:00", endTime = "22:00")
    )

    var isLoading = mutableStateOf(false)

//     val slotsList = mutableStateListOf<Slots>()

    private val _days = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    ).map { day -> mutableStateOf(SlotsDay(day, mutableListOf())) }

    val listOfDays = _days.map { it }
    var selectedSlots = mutableStateListOf<TimeSlot>()

    fun onEvent(event: SlotsEvent) {
        when (event) {
            is SlotsEvent.SetSlots -> setSlots(event.navController, event.context)
            is SlotsEvent.GetSlots -> getSlots()
        }
    }

//    fun getEverydaySlots() {
//        _days.forEach { day ->
//            viewModelScope.launch {
//                val slots = repo.getTimeSlot(day.value.day, "")
//                if (day.value.slots.isEmpty()) {
//                    day.value.slots.add(slots)
//                } else {
//                    day.value.slots[0] = slots
//                }
//            }
//        }
//    }

    private fun getSlots(){
        viewModelScope.launch {
//            openCloseTime.clear()
//            openCloseTime.addAll(repo.getTimeSlot())
            val newSlot=repo.getTimeSlot()
            for(slot in newSlot)
            openCloseTime.find { it.day == slot.day }
                ?.let { slots ->
                    val index = openCloseTime.indexOf(slots)
                    if (index != -1) {
                        openCloseTime[index]=slot
                    }
                }
        }
    }


//    fun addBookedSlots(timeSlot: TimeSlot, day: String) {
//        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.add(timeSlot.time)
//    }

//    fun removeBookedSlots(timeSlot: TimeSlot, day: String) {
//        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.remove(timeSlot.time)
//    }
//
//    fun addNotAvailableSlots(timeSlot: TimeSlot, day: String) {
//        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.add(timeSlot.time)
//    }
//
//    fun removeNotAvailableSlots(timeSlot: TimeSlot, day: String) {
//        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.remove(
//            timeSlot.time
//        )
//    }

//    fun addSlot(slots: Slots) {
//        _slotsList.add(slots)
//    }

    private fun setSlots(navController: NavController, context: Context) {
        viewModelScope.launch {
            repo.setSlots(openCloseTime).collect {
                onComplete(it, navController, context)

            }
        }
    }

//    suspend fun updateBookedSlotsFb(day: String, activity: Activity): Flow<Resource<String>> {
//        val bookedTimes =
//            _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.toList()
//                ?: listOf()
//        return repo.updateBookedSlots(bookedTimes, day)
//    }
//
//    suspend fun updateNotAvailableSlotsFb(day: String, activity: Activity): Flow<Resource<String>> {
//        val notAvailableTimes =
//            _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.toList()
//                ?: listOf()
//        return repo.updateNotAvailableSlots(notAvailableTimes, day)
//    }

    private fun onComplete(it: Resource<String>, navController: NavController, context: Context) {
        when (it) {
            is Resource.Loading -> {
                isLoading.value = true
            }
            is Resource.Success -> {
                isLoading.value = false

                navController.navigate(Screens.Home.route) {
                    popUpTo(Screens.SlotAdderScr.route) {
                        inclusive = true
                    }
                }
            }
            is Resource.Failure -> {
                isLoading.value = false
                Toast.makeText(context, "Error: ${it}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

sealed class SlotsEvent {
    data class SetSlots(val navController: NavController, val context: Context) : SlotsEvent()
    data object GetSlots : SlotsEvent()
}