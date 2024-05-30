package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.TimeSlots
import com.example.sallonappbarbar.data.model.WeekDay
import com.example.sallonappbarbar.ui.theme.Purple80
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavController,
    viewModel: BarberDataViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var isBarberShopOpen by remember { mutableStateOf(false) }
    var isOpenOrClose by remember { mutableStateOf("Open") }
    var openTime by remember {
        mutableStateOf(LocalTime.of(9,0).toString())
    }
    var closeTime by remember {
        mutableStateOf(LocalTime.of(21, 0).toString())
    }
    var isLoading by remember { mutableStateOf(false) }
    var slots: SnapshotStateList<TimeSlots> by mutableStateOf(
        mutableStateListOf(
            TimeSlots("9:00", "10:00")
        )
    )
    val barberData by viewModel.barberData.collectAsState()
    val barberSlots by viewModel.barberSlots.collectAsState()
    val openCloseTime by viewModel.openCloseTime.collectAsState()
    val openTimeDialogState = rememberMaterialDialogState()
    val closeTimeDialogState = rememberMaterialDialogState()
    val confirmOpenCloseDialogState = rememberMaterialDialogState()

    if (isLoading) {
        CircularProgressWithAppLogo()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetchBarberData()
    }

    when (barberData) {
        is Resource.Success -> {
            isLoading = false
            Toast.makeText(activity, "Data Loaded", Toast.LENGTH_SHORT).show()
            val data = (barberData as Resource.Success).result
            isBarberShopOpen = data.open ?: false
            isOpenOrClose = if (isBarberShopOpen) "Open" else "Close"
            LaunchedEffect(Unit) {
                viewModel.fetchBarberSlots()
            }
        }
        is Resource.Failure -> {
            isLoading = false
            Toast.makeText(activity, "Error fetching data: ${(barberData as Resource.Failure).exception.message}", Toast.LENGTH_SHORT).show()
        }
        is Resource.Loading -> {
            isLoading = true
        }
    }

    when (barberSlots) {
        is Resource.Success -> {
            isLoading = false
            val slotsData = (barberSlots as Resource.Success).result
            if (slotsData.isNotEmpty()) {
                slots.clear()
                slots.addAll(slotsData[0].slots)
                Toast.makeText(activity, "Slots Loaded", Toast.LENGTH_LONG).show()
                LaunchedEffect(Unit) {
                    viewModel.fetchOpenCloseTime()
                }
            } else {
                Toast.makeText(activity, "No slots available", Toast.LENGTH_LONG).show()
            }
        }
        is Resource.Failure -> {
            isLoading = false
            Toast.makeText(activity, "Error fetching data: ${(barberSlots as Resource.Failure).exception.message}", Toast.LENGTH_SHORT).show()
        }
        is Resource.Loading -> {
            isLoading = true
        }
    }

    when (openCloseTime) {
        is Resource.Success -> {
            isLoading = false
            Toast.makeText(activity, "Time Loaded", Toast.LENGTH_SHORT).show()
            val time = (openCloseTime as Resource.Success).result.split(" - ")
            if (time.size == 2) {
                openTime = time[0]
                closeTime = time[1]
            } else {
                openTime = "N/A"
                closeTime = "N/A"
            }
        }
        is Resource.Failure -> {
            isLoading = false
            Toast.makeText(activity, "Error fetching data: ${(openCloseTime as Resource.Failure).exception.message}", Toast.LENGTH_SHORT).show()
        }
        is Resource.Loading -> {
            isLoading = true
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(Purple80.toArgb())),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Barber Shop",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = isOpenOrClose,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    Switch(
                        checked = isBarberShopOpen,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                isLoading = true
                                try {
                                    val result = withContext(Dispatchers.IO) {
                                        viewModel.isShopOpen(isChecked, activity)
                                    }
                                    withContext(Dispatchers.Main) {
                                        result.collect() {
                                            when (it) {
                                                is Resource.Success -> {
                                                    isLoading = false
                                                    Toast.makeText(
                                                        activity,
                                                        "Shop is ${if (isChecked) "Open" else "Close"}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    isBarberShopOpen = isChecked
                                                    isOpenOrClose =
                                                        if (isChecked) "Open" else "Close"
                                                }

                                                is Resource.Failure -> {
                                                    isLoading = false
                                                    // Handle data fetching error here (e.g., show a toast)
                                                    Toast.makeText(
                                                        activity,
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
                                    // Handle any unexpected errors here (e.g., network issues)
                                    isLoading = false
                                    Toast.makeText(
                                        activity,
                                        "Unexpected error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            uncheckedThumbColor = Color.Red,
                            checkedTrackColor = Color.Green.copy(alpha = 0.5f),
                            uncheckedTrackColor = Color.Red.copy(alpha = 0.5f),
                            checkedBorderColor = Color.Black,
                            uncheckedBorderColor = Color.Black
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Open: $openTime - $closeTime",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    openTimeDialogState.show()
                }
            ) {
                Text(text = "Change Open/Close Time")
            }
            Text(
                text = "${slots[0].startTime} - ${slots[0].endTime}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
        MaterialDialog(
            dialogState = openTimeDialogState,
            buttons = {
                negativeButton(text = "Cancel")
                positiveButton(text = "Next") {
                    closeTimeDialogState.show()
                }
            }
        ) {
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
        }
        MaterialDialog(
                dialogState = closeTimeDialogState,
                buttons = {
                    negativeButton(text = "Cancel")
                    positiveButton(text = "Next") {
                        confirmOpenCloseDialogState.show()
                    }
                }
            ) {
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
        MaterialDialog(
                    dialogState = confirmOpenCloseDialogState,
                    buttons = {
                        negativeButton(text = "Cancel")
                        positiveButton(text = "Confirm") {
                            scope.launch(Dispatchers.IO) {
                                viewModel.addOpenCloseTime(
                                    "$openTime - $closeTime",
                                    activity
                                ).collect { resource ->
                                    withContext(Dispatchers.Main) {
                                        when (resource) {
                                            is Resource.Success -> {
                                                isLoading = false
                                                Toast.makeText(
                                                    activity,
                                                    "Open/Close time added",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Failure -> {
                                                isLoading = false
                                                // Handle data fetching error here (e.g., show a toast)
                                                Toast.makeText(
                                                    activity,
                                                    "Error uploading data: ${resource.exception.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            is Resource.Loading -> {
                                                isLoading = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "Change the open/close time to \n $openTime - $closeTime ?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}