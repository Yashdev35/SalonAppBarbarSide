package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
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
import com.example.sallonappbarbar.appUi.components.BottomAppNavigationBar
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.NavigationItem
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
fun MainScreen1(navHostController: NavController,context: Context) {
    var selectedScreen by remember { mutableStateOf(NavigationItem.Home) }
    Scaffold(
        bottomBar = {
            BottomAppNavigationBar(
                selectedItem = selectedScreen,
                onItemSelected = { selectedScreen = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedScreen) {
                NavigationItem.Home -> TopScreen(navHostController,context)
                NavigationItem.Book -> androidx.compose.material3.Text("Book Screen")  // Placeholder for BookScreen
                NavigationItem.Message -> androidx.compose.material3.Text("Message Screen")  // Placeholder for MessageScreen
                NavigationItem.Profile -> androidx.compose.material3.Text("Profile Screen")  // Placeholder for ProfileScreen
                NavigationItem.Review -> androidx.compose.material3.Text("Review Screen")  // Placeholder for ReviewScreen
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(navController: NavController,context: Context){
    DoubleCard(
        midCarBody = {  },
        mainScreen = {
            HomeScreen(
                activity = context as Activity,
                navController = navController
            )
        },
        topAppBar = {

        },
//        bottomAppBar = {
//
//        }
    )
}

@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavController,
    viewModel: BarberDataViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isBarberShopOpen by remember { mutableStateOf(false) }
    var isOpenOrClose by remember { mutableStateOf("Open") }
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
            Toast.makeText(
                activity,
                "Error fetching data: ${(barberData as Resource.Failure).exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }

        is Resource.Loading -> {
            isLoading = true
        }
    }
    var selectedScreen by remember { mutableStateOf(NavigationItem.Home) }

}