package com.example.sallonappbarbar.appUi.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.data.FireStoreDbRepository
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.Service
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.data.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GetBarberDataViewModel @Inject constructor(
    private val repo: FireStoreDbRepository
) : ViewModel() {
    private var _slots = mutableStateOf(Slots("08:00", "22:00"))
    var slots: State<Slots> = _slots

    private var _barber = mutableStateOf(BarberModel())
    var barber: State<BarberModel> = _barber

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch { getCurrentBarber() }
    }
    private suspend fun getBarberWUid(uid: String) {
       viewModelScope.launch{ repo.getBarber(uid) }
    }

    private suspend fun updateBarber(barber: BarberModel,imageUri: Uri,context: Context,navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            repo.updateBarberInfo(barber, imageUri).collect {
                    resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Info Updated Successfully", Toast.LENGTH_SHORT).show()
                        getCurrentBarber()
                        navController.navigate(Screens.Home.route) {
                            navController.popBackStack()
                        }
                        _isLoading.value = false
                    }
                    is Resource.Failure -> {
                        getCurrentBarber()
                        navController.navigate(Screens.Home.route) {
                            navController.popBackStack()
                        }
                        _isLoading.value = false
                        Toast.makeText(context, "Failed to Update Info", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }


    suspend fun getCurrentBarber() {
        viewModelScope.launch {
            repo.getBarberData().collect {
                when (it) {
                    is Resource.Success -> {
                        _barber.value = it.result
                    }

                    is Resource.Failure -> {
                        _barber.value = BarberModel()
                    }

                    else -> {}
                }
            }
        }
    }
 fun onEvent(event: BarberEvent) {
        when (event) {
            is BarberEvent.getBarberById -> viewModelScope.launch{ getBarberWUid(event.barberId) }
            is BarberEvent.updateBarber -> viewModelScope.launch{ updateBarber(event.barber,event.imageUri,
                event.context,event.navController)}
            is BarberEvent.getBarberNearby -> {}
            is BarberEvent.getBarberPopular -> {}
            is BarberEvent.getServices -> {}
            is BarberEvent.setBooking -> {}
            else -> {}
            }
        }
    }
sealed class BarberEvent {
    data class getBarberById(val barberId : String) : BarberEvent()
    data class updateBarber(val barber: BarberModel,val imageUri: Uri,
                            var context: Context,
                            val navController: NavController) : BarberEvent()
    data class getBarberPopular(val city: String, val limit: Long) : BarberEvent()
    data class getBarberNearby(val city: String, val limit: Long) : BarberEvent()
    data class getServices(val uid: String) : BarberEvent()
//    data class getSlots(val day: String, val uid: String) : MainEvent2()
    data class setBooking(
        val barberuid: String,
        val useruid: String,
        val service: List<Service>,
        val gender: List<Int>,
        val date: LocalDate,
        val times: MutableState<List<TimeSlot>>
    ) : BarberEvent()

}
