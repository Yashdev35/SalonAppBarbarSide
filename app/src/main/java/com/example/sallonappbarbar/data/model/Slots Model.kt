package com.example.sallonappbarbar.data.model

import androidx.compose.runtime.snapshots.SnapshotStateList

data class TimeSlots(val startTime: String, val endTime: String)

data class WeekDay(val name: String, val slots: SnapshotStateList<TimeSlots>)


