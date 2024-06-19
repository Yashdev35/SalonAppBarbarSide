package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import com.example.sallonappbarbar.appUi.viewModel.RestScreenViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.data.model.WorkDay
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale

class MockRestScreenViewModel : RestScreenViewModel() {
    // Override necessary properties and methods for the preview
    override var selectedSlots = mutableStateListOf<TimeSlot>()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var isLoading by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState()

    if (isLoading) {
        CommonDialog()
    }

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
        val weekDayList = getWeekdaysWithDates()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Tabs(pagerState = pagerState, weekDays = weekDayList)
            HorizontalPager(
                state = pagerState,
                count = weekDayList.size,
                modifier = Modifier.weight(1f)
            ) { page ->
                val (day, date) = weekDayList[page].entries.first()
                TimeSelection(
                    day = day,
                    date = date,
                    navController = navController,
                    barberUid = auth.currentUser?.uid ?: "",
                )
            }
        }
    }
}

fun getWeekdaysWithDates(): List<Map<String, LocalDate>> {
    val currentDate = LocalDate.now()
    return List(7) { i ->
        val date = currentDate.plusDays(i.toLong())
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        mapOf(dayOfWeek to date)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState, weekDays: List<Map<String, LocalDate>>) {
    val coroutineScope = rememberCoroutineScope()
    val visibleTabs = 7
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
            weekDay.entries.forEach { (day, date) ->
                Tab(
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                day,
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                date.format(DateTimeFormatter.ofPattern("d MMM")),
                                style = MaterialTheme.typography.body2
                            )
                        }
                    },
                    selected = pagerState.currentPage == actualIndex,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(actualIndex)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}