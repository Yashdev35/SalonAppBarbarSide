package com.example.sallonappbarbar.appUi.ScreensUi.initiators


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.viewModel.SlotsEvent
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@Composable
fun SlotAdderScreen(
    navController: NavController,
    viewModel: SlotsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    if (isDialog) {
        AlertDialog(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, sallonColor, RoundedCornerShape(20.dp))
                .background(Color.White),
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    viewModel.onEvent(SlotsEvent.SetSlots(navController, context))
                }, colors = ButtonDefaults.buttonColors(containerColor = sallonColor)) {
                    Text(
                        "Add", color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {

                    }, colors = ButtonDefaults.buttonColors(containerColor = sallonColor)
                ) {
                    Text(
                        "Cancel", color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            },
            containerColor = Color.White,
            title = { Text("Confirmation", color = Color.Black) },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to add these slots. If any slots are empty then default time is added and you can change the slot later.",
                        color = Color.Black
                    )
                }
            }
        )
    }
    if (viewModel.isLoading.value) {
        CommonDialog()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(purple_200.toArgb())
            )

    ) {
        BackButtonTopAppBar(onBackClick = { /*TODO*/ }, title = "Opening and Closing Time")
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color.White)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                viewModel.openCloseTime.forEach { weekDay ->
                    DayCard(slot = weekDay, viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    isDialog = true
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
fun DayCard(slot: Slots, viewModel: SlotsViewModel) {
    val openTimeDialog = rememberMaterialDialogState()
    val context = LocalContext.current
    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }



    Card(

        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor = Color.White,
        ), border = BorderStroke(2.dp, sallonColor)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = slot.day,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Button(
                    onClick = { openTimeDialog.show() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(sallonColor.toArgb())
                    )
                ) {
                    Text(
                        if (openTime == "") "Add Time" else "Change Time",
                        color = Color.White,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            if (openTime != "") {
                Text(
                    text = "$openTime -${closeTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

        }
        MaterialDialog(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, sallonColor),
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
                    val normalizedCloseTime =
                        if (closeTimeHours == 0 && closeTimeMinutes == 0) 1440 else closeTimeHours * 60 + closeTimeMinutes

                    if (normalizedOpenTime < normalizedCloseTime) {
                        if ((openTimeMinutes == 0 || openTimeMinutes == 30) && (closeTimeMinutes == 0 || closeTimeMinutes == 30)) {
                            val index = viewModel.openCloseTime.indexOfFirst { it.day == slot.day }
                            viewModel.openCloseTime[index].startTime = openTime
                            viewModel.openCloseTime[index].endTime = closeTime
                            Toast.makeText(
                                context,
                                (viewModel.openCloseTime[0]).toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Please select only 30 or 00 in minute",
                                Toast.LENGTH_SHORT
                            ).show()
                            openTimeDialog.show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Close time should be greater than open time",
                            Toast.LENGTH_SHORT
                        ).show()
                        openTimeDialog.show()
                    }
                }
                negativeButton("Cancel") {
                    openTime = ""
                    closeTime = ""
                }
            },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            )
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Open Time", color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.of(8, 0),
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ) {
                    openTime = String.format("%02d:%02d", it.hour, it.minute)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Close Time", color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                timepicker(
                    initialTime = LocalTime.of(22, 0),
                    timeRange = LocalTime.of(0, 0)..LocalTime.of(23, 59)
                ) {
                    closeTime = String.format("%02d:%02d", it.hour, it.minute)
                }
            }
        }
    }
}

//fun getDatesOfWeek(): Map<DayOfWeek, LocalDate> {
//    val today = LocalDate.now()
//    val datesOfWeek = mutableMapOf<DayOfWeek, LocalDate>()
//    for (i in 0..6) {
//        val date = today.plusDays(i.toLong())
//        datesOfWeek[date.dayOfWeek] = date
//    }
//    return datesOfWeek
//}