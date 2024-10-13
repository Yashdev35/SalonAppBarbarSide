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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.components.CommonDialog
import com.example.sallonappbarbar.appUi.viewModel.AllBarberInfoViewModel
import com.example.sallonappbarbar.appUi.viewModel.SlotsEvent
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import com.example.sallonappbarbar.data.model.Slots
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalTime

@Composable
fun SlotAdderScreen(
    navController: NavController,
    allBarberInfoViewModel: AllBarberInfoViewModel = hiltViewModel(),
    viewModel: SlotsViewModel = hiltViewModel(),
    isUpdating: Boolean = false
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()


    var isDialog by remember { mutableStateOf(false) }

    if (isDialog) {
        AlertDialog(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, sallonColor, RoundedCornerShape(20.dp))
                .background(Color.White),
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = {
                    scope.launch {
                        if (isUpdating) {
                            navController.navigate(Screens.Home.route) {
                                popUpTo(Screens.SlotAdderScr.route) { inclusive = true }
                            }
                        } else {
                            viewModel.onEvent(SlotsEvent.SetSlots(navController, context))
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = sallonColor)) {
                    Text(
                        if (isUpdating) "Done" else "Add",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = sallonColor)
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
                    val message =
                        if (isUpdating) "Do you want to update the slots?" else "Are you sure you want to add these slots. If any slots are empty then default time is added and you can change the slot later."
                    Text(
                        text = message,
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
                allBarberInfoViewModel.openCloseTime.forEach { weekDay ->
                    DayCard(
                        slot = weekDay,
                        viewModel,
                        isUpdating = isUpdating,
                        allBarberInfoViewModel = allBarberInfoViewModel
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.height(80.dp))
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
fun DayCard(
    slot: Slots,
    viewModel: SlotsViewModel,
    allBarberInfoViewModel: AllBarberInfoViewModel,
    isUpdating: Boolean
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val openTimeDialog = rememberMaterialDialogState()

    // Using state to hold the initial values
    var openTime by remember { mutableStateOf("") }
    var closeTime by remember { mutableStateOf("") }

    var isLoading by remember { viewModel.isLoading }
    LaunchedEffect(slot, isUpdating) {
        if (isUpdating) {
            // Find the matching slot in allBarberInfoViewModel
            val index = allBarberInfoViewModel.openCloseTime.indexOfFirst { it.day == slot.day }
            if (index != -1) {
                openTime = allBarberInfoViewModel.openCloseTime[index].startTime
                closeTime = allBarberInfoViewModel.openCloseTime[index].endTime
            }
        }
    }

    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor = Color.White,
        ),
        border = BorderStroke(2.dp, sallonColor)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
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
                        if (openTime.isEmpty()) "Add Time" else "Change Time",
                        color = Color.White,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            if (openTime.isNotEmpty()) {
                Text(
                    text = "$openTime - $closeTime",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        // Time Picker Dialog
        MaterialDialog(
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, sallonColor),
            dialogState = openTimeDialog,
            buttons = {
                positiveButton("Confirm") {
                    val openTimeParts = openTime.split(":").map { it.toInt() }
                    val closeTimeParts = closeTime.split(":").map { it.toInt() }

                    val normalizedOpenTime = openTimeParts[0] * 60 + openTimeParts[1]
                    val normalizedCloseTime =
                        if (closeTimeParts[0] == 0 && closeTimeParts[1] == 0) {
                            1440 // Midnight
                        } else {
                            closeTimeParts[0] * 60 + closeTimeParts[1]
                        }

                    if (normalizedOpenTime < normalizedCloseTime) {
                        if ((openTimeParts[1] == 0 || openTimeParts[1] == 30) && (closeTimeParts[1] == 0 || closeTimeParts[1] == 30)) {
                            if (!isUpdating) {
                                val index =
                                    viewModel.openCloseTime.indexOfFirst { it.day == slot.day }
                                if (index != -1) {
                                    viewModel.openCloseTime[index].startTime = openTime
                                    viewModel.openCloseTime[index].endTime = closeTime
                                    Toast.makeText(context, "Times Updated", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
                                scope.launch {
                                    viewModel.onEvent(
                                        SlotsEvent.updateSlotTimes(
                                            slot.day,
                                            openTime,
                                            closeTime,
                                            context
                                        )
                                    )
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please select only 30 or 00 in minutes",
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