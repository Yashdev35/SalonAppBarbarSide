package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.SlotStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TimeSlot(val time:  String, val date: String, val status: SlotStatus) : Parcelable

data class WorkDay(
    val name: String,
    val availableSlots: SnapshotStateList<TimeSlot>,
    val bookedSlots: SnapshotStateList<TimeSlot> = mutableStateListOf(),
    val unavailableSlots: SnapshotStateList<TimeSlot> = mutableStateListOf(),
    var date: String = LocalDate.now().toString(),
    val dayOpenTime: MutableState<String> = mutableStateOf("10:00"),
    val dayCloseTime: MutableState<String> = mutableStateOf("20:00")
)

data class Slots(
    val StartTime: String,
    val EndTime: String,
    val Booked: List<String>? = emptyList(),
    val NotAvailable: List<String>? = emptyList(),
    val date: String= LocalDate.now().toString()
)

@Parcelize
data class DateSlots(val date: LocalDate, val slots: List<TimeSlot>) : Parcelable