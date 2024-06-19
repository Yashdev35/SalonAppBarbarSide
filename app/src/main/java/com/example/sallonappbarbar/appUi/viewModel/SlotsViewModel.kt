package com.example.sallonappbarbar.appUi.viewModel

import android.app.Activity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.sallonappbarbar.data.FirestoreRepository
import com.example.sallonappbarbar.data.model.Slots
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SlotsViewModel @Inject constructor(
    private val repo: FirestoreRepository
) :ViewModel(){
private val slotsList = mutableStateListOf<Slots>()
    var _slotsList :SnapshotStateList<Slots> = slotsList

    fun addSlot(slots:Slots){
        slotsList.add(slots)
    }
    fun removeSlot(slots: Slots){
        slotsList.remove(slots)
    }
    fun updateBookedSlots(times:List<String>, date:String){
        slotsList.find { it.date == date }?.Booked = times
    }
    fun updateNotAvailableSlots(times:List<String>, date:String){
        slotsList.find { it.date == date }?.NotAvailable = times
    }
    suspend fun setSlots(slotList : List<Slots>,activity: Activity)= repo.setSlots(slotList)
    suspend fun updateBookedSlotsFb(times:List<String>, day:String,activity: Activity)= repo.updateBookedSlots(times,day)
    suspend fun updateNotAvailableSlotsFb(times:List<String>, day:String,activity: Activity)= repo.updateNotAvailableSlots(times,day)

}