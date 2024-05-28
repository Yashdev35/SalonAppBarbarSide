package com.example.sallonappbarbar.appUi.Screens.MainScreens


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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.data.model.WeekDay
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

@Composable
fun SlotAdderScreen(
    activity: Activity,
    barberDataViewModel: BarberDataViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var weekDayList by remember {
        mutableStateOf(
            listOf(
                WeekDay("Monday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Tuesday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Wednesday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Thursday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Friday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Saturday", mutableStateListOf(TimeSlot("10:00", "12:00"))),
                WeekDay("Sunday", mutableStateListOf(TimeSlot("10:00", "12:00")))
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
            weekDayList.forEach { weekDay ->
                DayCard(weekDay = weekDay)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val allSlotsPresent = weekDayList.all{
                        it.slots.getOrNull(1)?.startTime?.isNotEmpty() ?: false && it.slots.getOrNull(1)?.endTime?.isNotEmpty() ?: false }

                    if(
                        allSlotsPresent
                    ){
                        scope.launch {
                            val finalList =  listOf(
                                WeekDay("Monday", mutableStateListOf(weekDayList[0].slots[1])),
                                WeekDay("Tuesday", mutableStateListOf(weekDayList[1].slots[1])),
                                WeekDay("Wednesday", mutableStateListOf(weekDayList[2].slots[1])),
                                WeekDay("Thursday", mutableStateListOf(weekDayList[3].slots[1])),
                                WeekDay("Friday", mutableStateListOf(weekDayList[4].slots[1])),
                                WeekDay("Saturday", mutableStateListOf(weekDayList[5].slots[1])),
                                WeekDay("Sunday", mutableStateListOf(weekDayList[6].slots[1]))
                            )
                            isLoading = true
                            try {
                                val result = withContext(Dispatchers.IO) {
                                    barberDataViewModel.addSlots(finalList, activity)
                                }
                                withContext(Dispatchers.Main) {
                                    result.collect {
                                        when (it) {
                                            is Resource.Success -> {
                                                isLoading = false
                                                navController.navigate(Screenes.Home.route)
                                            }
                                            is Resource.Failure -> {
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    "Error fetching data: ${it.exception.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                isLoading = true
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Unexpected error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
fun DayCard(weekDay: WeekDay) {
    val openTimeDialog = rememberMaterialDialogState()
    val confirmDialog = rememberMaterialDialogState()
    val context = LocalContext.current

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
                    text = weekDay.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Button(
                    onClick = {openTimeDialog.show()},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(sallonColor.toArgb())
                    )
                ) {
                    Text("Add a Slot",
                        color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            weekDay.slots.getOrNull(1)?.let { slot ->
                Text(
                    text = "${slot.startTime} - ${slot.endTime}",
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
                    weekDay.slots.add(TimeSlot(openTime, closeTime))
                    confirmDialog.show()
                }
                negativeButton("Cancel") {
                    openTime = ""
                    closeTime = ""
                }
            }
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
                    if (it.minute < 10) {
                        openTime = "${it.hour}:0${it.minute}"
                    } else {
                        openTime = "${it.hour}:${it.minute}"
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Close Time")
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Select close time",
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ) {
                    if (it.minute < 10) {
                        closeTime = "${it.hour}:0${it.minute}"
                    } else {
                        closeTime = "${it.hour}:${it.minute}"
                    }
                }
            }
        }
        MaterialDialog(
            dialogState = confirmDialog,
            buttons = {
                positiveButton("Ok") {
                    confirmDialog.hide()
                    if(weekDay.slots.size > 2){
                        Toast.makeText(context, "You can only add 1 slot", Toast.LENGTH_SHORT).show()
                        weekDay.slots.removeLast()
                    }
                }
                negativeButton("Cancel") {
                    weekDay.slots.removeLast()
                    confirmDialog.hide()
                }
            }
        ) {
            Text("Add this slot: $openTime - $closeTime?")
        }
    }
}

@Preview
@Composable
fun ScheduleScreenPreview() {
}
