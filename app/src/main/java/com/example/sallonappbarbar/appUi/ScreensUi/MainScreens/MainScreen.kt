package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.components.BottomAppNavigationBar
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.MessageViewModel
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel

@Composable
fun MainScreen(
    orderViewModel: OrderViewModel = hiltViewModel(),
    messageViewModel: MessageViewModel = hiltViewModel(),
    navHostController: NavController,
    barberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
) {
    val newChat by messageViewModel.newChat.collectAsState()


    Scaffold(
        bottomBar = {
            BottomAppNavigationBar(
                selectedItem = barberDataViewModel.navigationItem.value,
                onItemSelected = { barberDataViewModel.navigationItem.value = it },
                messageCount = newChat
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (barberDataViewModel.navigationItem.value) {
                NavigationItem.Home -> {
                    barberDataViewModel.navigationItem.value = NavigationItem.Home
                    HomeScreen(orderViewModel)
                }

                NavigationItem.Book -> {
                    barberDataViewModel.navigationItem.value = NavigationItem.Book
                    ScheduleScreen(navController = navHostController, barberDataViewModel)
                }

                NavigationItem.Message -> {
                    barberDataViewModel.navigationItem.value = NavigationItem.Message
                    MessageScreen(navHostController, messageViewModel, barberDataViewModel)
                }

                NavigationItem.Profile -> {
                    barberDataViewModel.navigationItem.value = NavigationItem.Profile
                    ProfileScreen(navHostController, barberDataViewModel, orderViewModel)
                }

                NavigationItem.Review -> {
                    barberDataViewModel.navigationItem.value = NavigationItem.Review
                    ReviewScreen(barberDataViewModel, orderViewModel)
                }
            }
        }
    }
}
