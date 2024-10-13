package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.components.CommonDialog
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.components.RowofDate
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.SlotsEvent
import com.example.sallonappbarbar.appUi.viewModel.SlotsViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    navController: NavController,

    barberDataViewModel: GetBarberDataViewModel,
    slotsViewModel: SlotsViewModel = hiltViewModel(),
) {
    BackHandler {
        barberDataViewModel.navigationItem.value = NavigationItem.Home
    }
    var isLoading by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 7 })
    val weekDayList = getWeekdaysWithDates()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        CommonDialog()
    }

    LaunchedEffect(Unit) {
        slotsViewModel.onEvent(SlotsEvent.GetSlots)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedDate = weekDayList[pagerState.currentPage].values.first()
    }

    DoubleCard(
        midCarBody = {
            selectedDate =
                daySelection()
        },
        navController = navController,
        topAppBar = {
            Text(
                text = "Update Slots",
                modifier = Modifier
                    .padding(40.dp, 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
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