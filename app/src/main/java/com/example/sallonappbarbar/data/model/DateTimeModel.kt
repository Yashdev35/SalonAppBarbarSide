package com.example.sallonappbarbar.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate
import android.os.Parcelable
import com.example.sallonappbarbar.appUi.Screens.MainScreens.SlotStatus
import kotlinx.parcelize.Parcelize


@Parcelize
data class TimeSlot( val time:  String, val date: String, val status: SlotStatus) : Parcelable

@Parcelize
data class DateSlots(val date: LocalDate, val slots: List<TimeSlot>) : Parcelable

data class Slots(
    val day:String,
    val StartTime: String,
    val EndTime: String,
    val Booked: List<String>? = emptyList(),
    val NotAvailable: List<String>? = emptyList(),
    val date: String= LocalDate.now().toString()
)


