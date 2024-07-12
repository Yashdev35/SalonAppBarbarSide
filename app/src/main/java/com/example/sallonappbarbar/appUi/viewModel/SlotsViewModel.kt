package com.example.sallonappbarbar.appUi.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.SlotsDay
import com.example.sallonappbarbar.data.model.TimeSlot
import com.practicecoding.sallonapp.appui.components.SuccessfullDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
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
    var isSuccessfulDialog = mutableStateOf(false)

//     val slotsList = mutableStateListOf<Slots>()

    private val _days = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    ).map { day -> mutableStateOf(SlotsDay(day, mutableListOf())) }

    var selectedSlots = mutableStateListOf<TimeSlot>()

    fun onEvent(event: SlotsEvent) {
        when (event) {
            is SlotsEvent.SetSlots -> setSlots(event.navController, event.context)
            is SlotsEvent.GetSlots -> getSlots()
        }
    }



    private fun getSlots(){
        viewModelScope.launch {
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




    private fun setSlots(navController: NavController, context: Context) {
        viewModelScope.launch {
            repo.setSlots(openCloseTime).collect {
                onComplete(it, navController, context)

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
//                isSuccessfulDialog.value=true
//                navController.navigate(Screens.Home.route) {
//                    popUpTo(Screens.SlotAdderScr.route) {
//                        inclusive = true
//                    }
//                }
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
}