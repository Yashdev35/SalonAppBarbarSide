package com.example.sallonappbarbar.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.time.LocalDate

data class TimeSlots(val startTime: String, val endTime: String)

data class WeekDay(
    val name: String,
    val availableSlots: SnapshotStateList<TimeSlots>,
    val bookedSlots: SnapshotStateList<TimeSlots> = mutableStateListOf(),
    val unavailableSlots: SnapshotStateList<TimeSlots> = mutableStateListOf(),
    val date: String = LocalDate.now().toString(),
    val dayOpenTime: MutableState<String> = mutableStateOf("10:00"),
    val dayCloseTime: MutableState<String> = mutableStateOf("20:00")
)

data class BarberSlot(
    val startTime: String,
    val status: Int
)


