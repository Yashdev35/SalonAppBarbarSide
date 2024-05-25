package com.example.sallonappbarbar.appUi.Screens.MainScreens


import android.app.Activity
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.data.model.WeekDay
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun SlotAdderScreen(
    activity: Activity,
    barberDataViewModel: BarberDataViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var weekDayList by remember {
        mutableStateOf(
            listOf(
                WeekDay("Monday", mutableListOf()),
                WeekDay("Tuesday", mutableListOf()),
                WeekDay("Wednesday", mutableListOf()),
                WeekDay("Thursday", mutableListOf()),
                WeekDay("Friday", mutableListOf()),
                WeekDay("Saturday", mutableListOf()),
                WeekDay("Sunday", mutableListOf())
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        weekDayList.forEach { weekDay ->
            DayCard(weekDay = weekDay)
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                barberDataViewModel.addSlots(activity= activity, weekDays = weekDayList)
            }
        }) {
            Text("Save")
        }
    }
}
@Composable
fun DayCard(weekDay: WeekDay) {
    val openTimeDialog = rememberMaterialDialogState()
    val confirmDialog = rememberMaterialDialogState()

    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = weekDay.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            weekDay.slots.forEach { slot ->
                Text(
                    text = "${slot.startTime} - ${slot.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { openTimeDialog.show() }) {
                Text("Add Slot")
            }
        }
        MaterialDialog(
            dialogState = openTimeDialog,
            buttons = {
                positiveButton("Confirm") {
                    weekDay.slots.add(TimeSlot(startTime = openTime, endTime = closeTime))
                    openTime = ""
                    closeTime = ""
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
                modifier = Modifier.wrapContentSize()
                    .verticalScroll(scrollState)
            ) {
                Text("Open Time")
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Select open time",
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ){
                    if(it.minute< 10){
                        openTime = "${it.hour}:0${it.minute}"
                    }else{
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
                ){
                    if(it.minute< 10){
                        closeTime = "${it.hour}:0${it.minute}"
                    }else{
                        closeTime = "${it.hour}:${it.minute}"
                    }
                }
            }
        }
        MaterialDialog(
            dialogState = confirmDialog,
            buttons = {
                positiveButton("Ok") {
                    openTimeDialog.hide()
                }
                negativeButton("Cancel") {
                    confirmDialog.hide()
                }
            }
        ) {
            Text("${weekDay.slots[weekDay.slots.size - 1].startTime} - ${weekDay.slots[weekDay.slots.size - 1].endTime} add this slot?")
        }
    }
}

@Preview
@Composable
fun ScheduleScreenPreview() {
}
