package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.ui.theme.Purple80
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavController,
    viewModel: BarberDataViewModel = hiltViewModel(),
) {
    var isBarberShopOpen by remember { mutableStateOf(false) }
    var isOpenOrClose by remember { mutableStateOf("Open") }
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        CommonDialog()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getBarberData(activity).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        isLoading = false
                        Toast.makeText(activity, "Data Loaded", Toast.LENGTH_SHORT).show()
                        val barberData = resource.result
                        isBarberShopOpen = barberData.open?.lowercase() == "yes"
                        isOpenOrClose = if (isBarberShopOpen) "Open" else "Close"
                    }
                    is Resource.Failure -> {
                        isLoading = false
                        // Handle data fetching error here (e.g., show a toast)
                        Toast.makeText(activity, "Error fetching data: ${resource.exception.message}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        isLoading = true
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(Purple80.toArgb())),
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
                                        result.collect(){
                                            when (it) {
                                                is Resource.Success -> {
                                                    isLoading = false
                                                    Toast.makeText(activity, "Shop is ${if (isChecked) "Open" else "Close"}", Toast.LENGTH_SHORT).show()
                                                    isBarberShopOpen = isChecked
                                                    isOpenOrClose = if (isChecked) "Open" else "Close"
                                                }
                                                is Resource.Failure -> {
                                                    isLoading = false
                                                    // Handle data fetching error here (e.g., show a toast)
                                                    Toast.makeText(activity, "Error fetching data: ${it.exception.message}", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(activity, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
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
        }
    }
}