package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.appUi.components.RowofDate
import com.example.sallonappbarbar.appUi.viewModel.RestScreenViewModel
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.aService
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.ceil

enum class SlotStatus {
    AVAILABLE, BOOKED, NOT_AVAILABLE
}

data class TimeSlot(val time: LocalTime, val status: SlotStatus)
data class WeekDay(val date: LocalDate, val isToday: Boolean)

@Composable
fun TimeSelection(
    startTime: LocalTime,
    endTime: LocalTime,
    intervalMinutes: Long,
    bookedTimes: List<LocalTime>,
    notAvailableTimes: List<LocalTime>,
    time: Int,
    date: LocalDate,
    navController: NavController,
    restScreenViewModel: RestScreenViewModel = hiltViewModel()
) {
    BackHandler {
        navController.popBackStack()
    }

    val context = LocalContext.current
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val slots = generateTimeSlots(startTime, endTime, intervalMinutes, bookedTimes, notAvailableTimes)
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    if (restScreenViewModel.selectedSlots.size > ceil(time / 30.0).toInt()) {
        showDialog = true
        dialogMessage = "You don't need more than ${ceil(time / 30.0).toInt()} slots"
        restScreenViewModel.selectedSlots.removeAt(restScreenViewModel.selectedSlots.size - 1)
    }

    val materialDialogState = rememberMaterialDialogState()

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // UI code remains the same
        slots.chunked(4).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { slot ->
                    TimeSlotBox(
                        slot = slot,
                        timeFormatter = timeFormatter,
                        isSelected = restScreenViewModel.selectedSlots.contains(slot),
                        onClick = {
                            when (slot.status) {
                                SlotStatus.AVAILABLE -> {
                                    if (restScreenViewModel.selectedSlots.contains(slot)) {
                                        restScreenViewModel.selectedSlots.remove(slot)
                                    } else {
                                        restScreenViewModel.selectedSlots.add(slot)
                                    }
                                }
                                SlotStatus.BOOKED, SlotStatus.NOT_AVAILABLE -> {
                                    showDialog = true
                                    dialogMessage = if (slot.status == SlotStatus.BOOKED) {
                                        "This slot is already booked."
                                    } else {
                                        "This slot is not available."
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }

        GeneralButton(text = "Update Slots", width = 300, height = 50, modifier = Modifier) {

        }

        Text(
            text = "Estimated total time you need according to your selection is approx $time mins",
            color = Color.Green, // Replace with actual color
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            fontSize = 12.sp
        )

        MaterialDialog(
            dialogState = materialDialogState,
            buttons = {
                positiveButton(text = "Make available for booking") { /* Handle logic */ }
                positiveButton(text = "Make unavailable for booking") { /* Handle logic */ }
                negativeButton(text = "Cancel") { materialDialogState.hide() }
            }
        ) {
            Text("Dialog content")
        }
    }
}

@Composable
fun TimeSlotBox(slot: TimeSlot, timeFormatter: DateTimeFormatter, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
        isSelected -> Color.Green
        slot.status == SlotStatus.AVAILABLE -> Color.Gray
        slot.status == SlotStatus.BOOKED -> Color.Red // Replace with actual color
        slot.status == SlotStatus.NOT_AVAILABLE -> Color.DarkGray
        else -> Color.Transparent
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(width = 80.dp, height = 40.dp)
            .padding(4.dp)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = slot.time.format(timeFormatter),
                color = backgroundColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun generateTimeSlots(
    startTime: LocalTime,
    endTime: LocalTime,
    intervalMinutes: Long,
    bookedTimes: List<LocalTime>,
    notAvailableTimes: List<LocalTime>
): List<TimeSlot> {
    val slots = mutableListOf<TimeSlot>()
    var currentTime = startTime

    while (currentTime.isBefore(endTime) || currentTime == endTime) {
        val status = when {
            notAvailableTimes.contains(currentTime) -> SlotStatus.NOT_AVAILABLE
            bookedTimes.contains(currentTime) -> SlotStatus.BOOKED
            else -> SlotStatus.AVAILABLE
        }
        slots.add(TimeSlot(currentTime, status))
        currentTime = currentTime.plus(intervalMinutes, ChronoUnit.MINUTES)
    }

    return slots
}


@Preview(showBackground = true)
@Composable
fun PreviewTimeSlotsGrid() {
    val startTime = LocalTime.of(10, 0)
    val endTime = LocalTime.of(22, 0)
    val intervalMinutes = 30L
    val bookedTimes = listOf(
        LocalTime.of(11, 0),
        LocalTime.of(13, 30),
        LocalTime.of(15, 0)
    )
    val notAvailableTimes = listOf(
        LocalTime.of(12, 30),
        LocalTime.of(16, 0)
    )
    val navController = rememberNavController()
    val aService = listOf(
        aService(
            price = "100",
            serviceTypeHeading = "Haircut",
            serviceName = "Haircut",
            id = 1,
            time = "30"
        )
    )
    val barberModel = BarberModel()
    val genders = listOf(1, 2)
}

