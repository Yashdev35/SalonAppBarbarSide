package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.GeneralButton
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class SlotStatus {
    AVAILABLE, BOOKED, NOT_AVAILABLE
}
@Composable
fun TimeSelection(
    day : String,
    date: LocalDate,
    navController: NavController,
    barberUid: String,
    viewModel: GetBarberDataViewModel = hiltViewModel(),
    slotsViewModel: SlotsViewModel = hiltViewModel()
) {
    BackHandler {
        navController.popBackStack()
    }
    LaunchedEffect(date) {
        viewModel.getSlots(day,barberUid)
    }
    val scope = rememberCoroutineScope()
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val context = LocalContext.current
    val slotTime = viewModel.slots.value
    val startTime = LocalTime.parse(slotTime.StartTime, timeFormatter)
    val endTime = LocalTime.parse(slotTime.EndTime, timeFormatter)

    val bookedTimes = remember {
        mutableStateListOf<LocalTime>()
    }
    var isLoading by remember { mutableStateOf(false) }
    if(isLoading){
        CommonDialog()
    }
    val notAvailableTimes = remember {
        mutableStateListOf<LocalTime>()
    }
if (date==LocalDate.parse(slotTime.date)) {
    bookedTimes.clear()
    for (timeString in slotTime.Booked!!) {
        val localTime = LocalTime.parse(timeString, timeFormatter)
        bookedTimes.add(localTime)
    }
}
    if (date==LocalDate.parse(slotTime.date)) {
        notAvailableTimes.clear()
    for (timeString in slotTime.NotAvailable!!) {
        val localTime = LocalTime.parse(timeString, timeFormatter)
        notAvailableTimes.add(localTime)
    }}

    val slots = generateTimeSlots(date,startTime, endTime, 30L, bookedTimes, notAvailableTimes)
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize()){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "AVAILABLE",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "NOT AVAILABLE",
                        color = Color.Red,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "BOOKED",
                        color = sallonColor, // Update to match `sallonColor` if defined
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "SELECTED",
                        color = Color.Green,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
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
                        isSelected = viewModel.selectedSlots.contains(slot),
                        onClick = {
                            when (slot.status) {
                                SlotStatus.AVAILABLE -> {
                                    if (viewModel.selectedSlots.contains(slot)) {
                                        viewModel.selectedSlots.remove(slot)
                                    } else if(viewModel.selectedSlots.size>0&&viewModel.selectedSlots[0].date!=date.toString()){
                                        showDialog=true
                                        dialogMessage = "You can select Slot for only one day"
                                    }else {
                                        viewModel.selectedSlots.add(slot)
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

        AnimatedVisibility(showDialog, enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Selection Error") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Text(
            text = "Tap the respective day above to edit the slots of that day.",
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline,
            fontSize = 16.sp
        )
    }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GeneralButton(text = "Add to booked", width = 150, height = 50, modifier = Modifier) {
                if (viewModel.selectedSlots.isNotEmpty()){
                    val selectedSlots = viewModel.selectedSlots.map { it.time }
                    scope.launch {
                        slotsViewModel.updateBookedSlotsFb(selectedSlots, day,context as Activity).collect{
                            when(it){
                                is Resource.Loading -> {
                                    isLoading = true
                                }
                                is Resource.Success -> {
                                    isLoading = false
                                    slotsViewModel.updateBookedSlots(selectedSlots, day)
                                    Toast.makeText(context, "Slots added to booked", Toast.LENGTH_SHORT).show()
                                    for (timeSlot in viewModel.selectedSlots){
                                        slots.map{slot->
                                            if(slot.time == timeSlot.time){
                                                slot.status = SlotStatus.BOOKED
                                            }
                                        }
                                    }
                                    viewModel.selectedSlots.clear()
                                }
                                is Resource.Failure -> {
                                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                }
            }
            GeneralButton(text = "Add to not available", width = 160, height = 50, modifier = Modifier) {
                if (viewModel.selectedSlots.isNotEmpty()){
                    val selectedSlots = viewModel.selectedSlots.map { it.time }
                    scope.launch {
                        slotsViewModel.updateNotAvailableSlotsFb(selectedSlots, day,context as Activity).collect{
                            when(it){
                                is Resource.Loading -> {
                                    isLoading = true
                                }
                                is Resource.Success -> {
                                    isLoading = false
                                    slotsViewModel.updateNotAvailableSlots(selectedSlots, day)
                                    Toast.makeText(context, "Slots added to not Available", Toast.LENGTH_SHORT).show()
                                    for (timeSlot in viewModel.selectedSlots){
                                        slots.map{slot->
                                            if(slot.time == timeSlot.time){
                                                slot.status = SlotStatus.NOT_AVAILABLE
                                            }
                                        }
                                    }
                                    viewModel.selectedSlots.clear()
                                }
                                is Resource.Failure -> {
                                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TimeSlotBox(
    slot: TimeSlot,
    timeFormatter: DateTimeFormatter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isSelected -> Color.Green
        slot.status == SlotStatus.AVAILABLE -> Color.Gray
        slot.status == SlotStatus.BOOKED -> sallonColor
        slot.status == SlotStatus.NOT_AVAILABLE -> Color.Red
        else -> Color.Transparent
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(purple_200, purple_200, purple_200, purple_200),
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
    date:LocalDate,
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
        slots.add(TimeSlot(currentTime.toString(), date.toString(),status))
        currentTime = currentTime.plus(intervalMinutes, ChronoUnit.MINUTES)
    }

    return slots
}

