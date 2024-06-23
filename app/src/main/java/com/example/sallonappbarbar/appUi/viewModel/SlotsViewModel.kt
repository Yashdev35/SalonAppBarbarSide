package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _slotsList = mutableStateListOf<Slots>()
    val slotsList: SnapshotStateList<Slots> = _slotsList

    private val _days = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    ).map { day -> mutableStateOf(SlotsDay(day, mutableListOf())) }

    val listOfDays = _days.map { it }

    fun getEverydaySlots() {
        _days.forEach { day ->
            viewModelScope.launch {
                val slots = repo.getTimeSlot(day.value.day, "")
                if (day.value.slots.isEmpty()) {
                    day.value.slots.add(slots)
                } else {
                    day.value.slots[0] = slots
                }
            }
        }
    }

    fun addBookedSlots(timeSlot: TimeSlot, day: String) {
        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.add(timeSlot.time)
    }

    fun removeBookedSlots(timeSlot: TimeSlot, day: String) {
        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.remove(timeSlot.time)
    }

    fun addNotAvailableSlots(timeSlot: TimeSlot, day: String) {
        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.add(timeSlot.time)
    }

    fun removeNotAvailableSlots(timeSlot: TimeSlot, day: String) {
        _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.remove(timeSlot.time)
    }

    fun addSlot(slots: Slots) {
        _slotsList.add(slots)
    }

    suspend fun setSlots(slotList: List<Slots>, activity: Activity) = repo.setSlots(slotList)
    suspend fun updateBookedSlotsFb(day: String, activity: Activity) : Flow<Resource<String>>{
            val bookedTimes = _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.Booked?.toList() ?: listOf()
             return repo.updateBookedSlots(bookedTimes, day)
    }
    suspend fun updateNotAvailableSlotsFb(day: String, activity: Activity) : Flow<Resource<String>> {
        val notAvailableTimes = _days.find { it.value.day == day }?.value?.slots?.getOrNull(0)?.NotAvailable?.toList() ?: listOf()
         return repo.updateNotAvailableSlots(notAvailableTimes, day)
    }
}