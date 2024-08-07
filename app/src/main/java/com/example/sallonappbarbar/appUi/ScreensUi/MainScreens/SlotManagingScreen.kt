package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import java.time.LocalDate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.RowofDate
import com.example.sallonappbarbar.appUi.viewModel.SlotsEvent
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    slotsViewModel: SlotsViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 7 })
    val weekDayList = getWeekdaysWithDates()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        CommonDialog()
    }

    LaunchedEffect(Unit){
        slotsViewModel.onEvent(SlotsEvent.GetSlots)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedDate = weekDayList[pagerState.currentPage].values.first()
    }

    DoubleCard(
        midCarBody = {selectedDate=
            daySelection(
//                selectedDate = selectedDate,
//                onDateSelected = { date ->
//                    selectedDate = date
//                    // Launch coroutine to scroll pagerState to the new page
//                    coroutineScope.launch {
//                        val newPageIndex = weekDayList.indexOfFirst { it.containsValue(date) }
//                        if (newPageIndex != -1) {
//                            pagerState.scrollToPage(newPageIndex)
//                        }
//                    }
//                }
            )
        },
        navController = navController,
        topAppBar = {},
        bottomAppBar = {},
        mainScreen = {
            TimeSelection(
                selectedDate,
                navController
            )
        }
    )
}

fun getWeekdaysWithDates(): List<Map<String, LocalDate>> {
    val currentDate = LocalDate.now()
    return List(7) { i ->
        val date = currentDate.plusDays(i.toLong())
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        mapOf(dayOfWeek to date)
    }
}

@Composable
fun daySelection(): LocalDate {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val currentDate = LocalDate.now()
    val monthName = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val year = currentDate.year
    val daysToShow = (0..6).map { currentDate.plusDays(it.toLong()) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$monthName $year",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (date in daysToShow) {
                val isSelected = date == selectedDate
                RowofDate(
                    isSelected = isSelected,
                    date = date.dayOfMonth.toString(),
                    day = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                ) {
                    selectedDate = date
                }
            }
        }

    }
    return selectedDate

}