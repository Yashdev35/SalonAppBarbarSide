package com.example.sallonappbarbar.appUi.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.model.Service
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel(){
    var selectedSlots = mutableStateListOf<TimeSlot>()

    private var slots = mutableStateOf(Slots("08:00", "22:00"))
    var _slots: State<Slots> = slots

    suspend fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.getBarberNearby -> {}
            is MainEvent.getBarberPopular -> {}
            is MainEvent.getServices -> {}
            is MainEvent.getSlots -> getSlots(event.day, event.uid)
            is MainEvent.setBooking -> {}

            else -> {}
        }
    }

    suspend fun getSlots(day: String, uid: String) {
        viewModelScope.launch { slots.value = repo.getTimeSlot(day, uid) }
    }
}
sealed class MainEvent {
    data class getBarberPopular(val city: String, val limit: Long) : MainEvent()
    data class getBarberNearby(val city: String, val limit: Long) : MainEvent()
    data class getServices(val uid: String) : MainEvent()
    data class getSlots(val day: String, val uid: String) : MainEvent()
    data class setBooking(
        val barberuid: String,
        val useruid: String,
        val service: List<Service>,
        val gender: List<Int>,
        val date: LocalDate,
        val times: MutableState<List<TimeSlot>>
    ) : MainEvent()
}