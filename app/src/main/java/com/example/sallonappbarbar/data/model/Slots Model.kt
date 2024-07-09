package com.example.sallonappbarbar.data.model

import android.os.Parcelable
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.SlotStatus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TimeSlot(val time:  String, val date: String, var status: SlotStatus) : Parcelable

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