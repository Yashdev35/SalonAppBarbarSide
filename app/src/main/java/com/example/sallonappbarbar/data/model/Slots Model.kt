package com.example.sallonappbarbar.data.model

import androidx.compose.runtime.snapshots.SnapshotStateList

data class TimeSlot(val startTime: String, val endTime: String)

data class WeekDay(val name: String, val slots: SnapshotStateList<TimeSlot>)


