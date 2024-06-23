package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens


import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.data.model.WorkDay
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun SlotAdderScreen(
    activity: Activity,
    navController: NavController,
    slotViewModel: SlotsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var workDayLists by remember {
        mutableStateOf(
            listOf(
                WorkDay("Monday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Tuesday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Wednesday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Thursday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Friday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Saturday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE))),
                WorkDay("Sunday", mutableStateListOf(TimeSlot("10:00", "12:00",SlotStatus.AVAILABLE)))
            )
        )
    }
    if (isLoading) {
        CommonDialog()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(Purple80.toArgb())
                ).padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            workDayLists.forEach { weekDay ->
                DayCard(workDay = weekDay,slotViewModel)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val allSlotsPresent = workDayLists.all{
                        it.availableSlots.getOrNull(1)?.time?.isNotEmpty() ?: false }
                    if(allSlotsPresent){
                        scope.launch {
                            slotViewModel.setSlots(slotViewModel.slotsList,activity).collect {
                                when(it){
                                    is Resource.Loading -> {
                                        isLoading = true
                                    }
                                    is Resource.Success -> {
                                        isLoading = false
                                        scope.launch(Dispatchers.Main) {
                                            navController.navigate(Screens.Home.route){
                                                popUpTo(Screens.SlotAdderScr.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                    is Resource.Failure -> {
                                        isLoading = false
                                        Toast.makeText(context, "Error: ${it}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }else{
                        Toast.makeText(context, "Please add all slots", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(sallonColor.toArgb())
                )
            ) {
                Text(
                    "Save and Next",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}

@Composable
fun DayCard(workDay: WorkDay,slotViewModel: SlotsViewModel) {
    val openTimeDialog = rememberMaterialDialogState()
    val confirmDialog = rememberMaterialDialogState()
    val context = LocalContext.current
    val date = getDatesOfWeek()[DayOfWeek.valueOf(workDay.name.uppercase())]
    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(Purple40.toArgb())
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = workDay.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Button(
                    onClick = {openTimeDialog.show()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(sallonColor.toArgb())
                    )
                ) {
                    Text("➕ Open close time",
                        color = Color.White,
                        modifier = Modifier.padding(1.dp))
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            workDay.availableSlots.getOrNull(1)?.let { slot ->
                Text(
                    text = "${slot.time} -$closeTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        MaterialDialog(
            dialogState = openTimeDialog,
            buttons = {
                positiveButton("Confirm") {
                    val openTimeParts = openTime.split(":").map { it.toInt() }
                    val closeTimeParts = closeTime.split(":").map { it.toInt() }

                    val openTimeMinutes = openTimeParts[1]
                    val closeTimeMinutes = closeTimeParts[1]
                    val openTimeHours = openTimeParts[0]
                    val closeTimeHours = closeTimeParts[0]
                    val normalizedOpenTime = openTimeHours * 60 + openTimeMinutes
                    val normalizedCloseTime = if (closeTimeHours == 0 && closeTimeMinutes == 0) 1440 else closeTimeHours * 60 + closeTimeMinutes

                    if (normalizedOpenTime < normalizedCloseTime) {
                        if ((openTimeMinutes == 0 || openTimeMinutes == 30) && (closeTimeMinutes == 0 || closeTimeMinutes == 30)) {
                            workDay.availableSlots.add(TimeSlot(openTime, closeTime,SlotStatus.AVAILABLE))
                            val slot = Slots(
                                StartTime = openTime,
                                EndTime = closeTime,
                                day = workDay.name,
                                date = date.toString()
                            )
                            slotViewModel.addSlot(slot)
                            confirmDialog.show()
                        } else {
                            Toast.makeText(context, "Please select only 30 or 00 in minute", Toast.LENGTH_SHORT).show()
                            openTimeDialog.show()
                        }
                    } else {
                        Toast.makeText(context, "Close time should be greater than open time", Toast.LENGTH_SHORT).show()
                        openTimeDialog.show()
                    }
                }
                negativeButton("Cancel") {
                    openTime = ""
                    closeTime = ""
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false,
                dismissOnBackPress = false)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .verticalScroll(scrollState)
            ) {
                Text("Open Time")
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Select open time",
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ) {
                    openTime = String.format("%02d:%02d", it.hour, it.minute)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Close Time")
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Select close time",
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ) {
                    closeTime = String.format("%02d:%02d", it.hour, it.minute)
                }
            }
        }
        MaterialDialog(
            dialogState = confirmDialog,
            buttons = {
                positiveButton("Ok") {
                    confirmDialog.hide()
                    if(workDay.availableSlots.size > 2){
                        Toast.makeText(context, "You can only add 1 slot", Toast.LENGTH_SHORT).show()
                        workDay.availableSlots.removeLast()
                    }
                }
                negativeButton("Cancel") {
                    workDay.availableSlots.removeLast()
                    confirmDialog.hide()
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false,
                dismissOnBackPress = false)
        ) {
            Text(
                text = "Open close time for ${workDay.name} : $openTime - $closeTime?",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
fun getDatesOfWeek(): Map<DayOfWeek, LocalDate> {
    val today = LocalDate.now()
    val datesOfWeek = mutableMapOf<DayOfWeek, LocalDate>()
    for (i in 0..6) {
        val date = today.plusDays(i.toLong())
        datesOfWeek[date.dayOfWeek] = date
    }
    return datesOfWeek
}