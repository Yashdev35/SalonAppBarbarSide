package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.aService
import com.google.accompanist.pager.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.RestScreenViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.WorkDay
import com.google.accompanist.pager.*
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalTime

class MockRestScreenViewModel : RestScreenViewModel() {
    // Override necessary properties and methods for the preview
    override var selectedSlots = mutableStateListOf<TimeSlot>()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    restScreenViewModel: RestScreenViewModel = viewModel(),
    barberDataViewModel: BarberDataViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as Activity
    val workDaysState = remember { mutableStateOf<Resource<List<WorkDay>>>(Resource.Loading) }

    LaunchedEffect(Unit) {
        workDaysState.value = barberDataViewModel.retrieveSlots(activity).first()
    }

    val pagerState = rememberPagerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedule") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        when (workDaysState.value) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                val workDaysList = (workDaysState.value as Resource.Success<List<WorkDay>>).result
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Tabs(pagerState = pagerState, weekDays = workDaysList)
                    HorizontalPager(
                        state = pagerState,
                        count = workDaysList.size,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        val workDay = workDaysList[page]
                        TimeSelectionScreen(
                            workDay = workDay,
                            navController = navController,
                            restScreenViewModel = restScreenViewModel
                        )
                    }
                }
            }
            is Resource.Failure -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Failed to load work days")
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState, weekDays: List<WorkDay>) {
    val coroutineScope = rememberCoroutineScope()
    val visibleTabs = 4
    val startIndex = (pagerState.currentPage - visibleTabs / 2).coerceAtLeast(0)
    val endIndex = (startIndex + visibleTabs).coerceAtMost(weekDays.size)

    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        weekDays.subList(startIndex, endIndex).forEachIndexed { index, weekDay ->
            val actualIndex = startIndex + index
            Tab(
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            weekDay.date.format(DateTimeFormatter.ofPattern("E")),
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            weekDay.date.format(DateTimeFormatter.ofPattern("d MMM")),
                            style = MaterialTheme.typography.body2
                        )
                    }
                },
                selected = pagerState.currentPage == actualIndex,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(actualIndex)
                    }
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

fun generateWeekDays(): List<WeekDay> {
    val today = LocalDate.now()
    return (0..6).map { offset ->
        val date = today.plusDays(offset.toLong())
        WeekDay(date = date, isToday = date == today)
    }
}

@Composable
fun TimeSelectionScreen(
    workDay: WorkDay,
    navController: NavController,
    restScreenViewModel: RestScreenViewModel,
) {
    val startTime = LocalTime.parse(workDay.dayOpenTime.value)
    val endTime = LocalTime.parse(workDay.dayCloseTime.value)
    val bookedTimes = workDay.bookedSlots.map { LocalTime.parse(it.startTime) }
    val notAvailableTimes = workDay.unavailableSlots.map { LocalTime.parse(it.startTime) }

    TimeSelection(
        startTime = startTime,
        endTime = endTime,
        intervalMinutes = 30,
        bookedTimes = bookedTimes,
        notAvailableTimes = notAvailableTimes,
        time = 120,
        date = LocalDate.parse(workDay.date),
        navController = navController,
        restScreenViewModel = restScreenViewModel
    )
}


@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
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
    val mockViewModel = MockRestScreenViewModel()

    CompositionLocalProvider(LocalViewModelStoreOwner provides object : ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }) {
        ScheduleScreen(
            navController = navController,
            restScreenViewModel = mockViewModel
        )
    }
}

