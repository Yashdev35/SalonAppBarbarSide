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
data class TimeSlot(val time:  String, val date: String, var status: SlotStatus) : Parcelable

data class WorkDay(
    val name: String,
    val availableSlots: SnapshotStateList<TimeSlot>,
    val bookedSlots: SnapshotStateList<TimeSlot> = mutableStateListOf(),
    val unavailableSlots: SnapshotStateList<TimeSlot> = mutableStateListOf(),
    var date: String = LocalDate.now().toString(),
    val dayOpenTime: MutableState<String> = mutableStateOf("10:00"),
    val dayCloseTime: MutableState<String> = mutableStateOf("20:00")
)
@Parcelize
data class Slots(
    val StartTime: String,
    val EndTime: String,
    var Booked: MutableList<String>? = mutableListOf(),
    var NotAvailable: MutableList<String>? = mutableListOf(),
    val date: String= LocalDate.now().toString(),
    val day: String = LocalDate.now().dayOfWeek.toString()
): Parcelable

@Parcelize
data class DateSlots(val date: LocalDate, val slots: List<TimeSlot>) : Parcelable

@Parcelize
data class SlotsDay(val day: String, var slots: MutableList<Slots>) : Parcelable