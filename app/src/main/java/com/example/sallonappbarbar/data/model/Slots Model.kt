package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.SlotStatus
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Parcelize
data class TimeSlot(val time:  String, val date: String, var status: SlotStatus) : Parcelable

@Serializable
@Parcelize
data class Slots(
    var startTime: String,
    var endTime: String,
    var booked: MutableList<String>? = mutableListOf(),
    val notAvailable: MutableList<String>? = mutableListOf(),
    var date: String= LocalDate.now().toString(),
    val day: String = LocalDate.now().dayOfWeek.toString()
): Parcelable


@Parcelize
data class DateSlots(val date: LocalDate, val slots: List<TimeSlot>) : Parcelable

@Parcelize
data class SlotsDay(val day: String, var slots: MutableList<Slots>) : Parcelable