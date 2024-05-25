package com.example.sallonappbarbar.data.model

data class TimeSlot(val startTime: String, val endTime: String)

data class WeekDay(val name: String, val slots: MutableList<TimeSlot>)


