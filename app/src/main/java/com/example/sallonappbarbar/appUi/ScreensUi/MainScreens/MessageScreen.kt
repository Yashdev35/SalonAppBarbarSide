package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.MessageItemBox
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.MessageEvent
import com.example.sallonappbarbar.appUi.viewModel.MessageViewModel
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar

@Composable
fun MessageScreen(navHostController: NavController,viewModel: MessageViewModel,barberDataViewModel: GetBarberDataViewModel ) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    BackHandler {
        barberDataViewModel.navigationItem.value= NavigationItem.Home
    }
    DoubleCard(
        midCarBody = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = sallonColor,
                modifier = Modifier.padding(horizontal = 20.dp),

            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                        )
                        .background(if (selectedTabIndex == 0) purple_200 else Color.White)

                ) {
                    Text(
                        "Chat",
                        color = if (selectedTabIndex == 0) Color.Black else Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                        .background(if (selectedTabIndex == 1) purple_200 else Color.White)

                ) {
                    Text(
                        "Call",
                        color = if (selectedTabIndex == 1) Color.Black else Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        },
        mainScreen = {
            if(selectedTabIndex == 0) {
                MessageList(navHostController, viewModel)
            }
        },
        topAppBar = {
            Text( text = "Message",
                modifier = Modifier
                    .padding(40.dp, 26.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
        },
    )
}

@Composable
fun MessageList(navHostController: NavController,messageViewModel: MessageViewModel ){
    var refresh by remember {
        mutableStateOf(true)
    }
    val barberChat by messageViewModel.barberChat.collectAsState()


//    LaunchedEffect(refresh) {
//viewModel.onEvent(MessageEvent.GetChatBarber)
//    }
    if (barberChat.isNotEmpty()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            barberChat.sortedByDescending { it.message.time }.forEach { chatModel ->
                MessageItemBox(
                    navHostController = navHostController,

                    barber = chatModel,
                    messageViewModel = messageViewModel
                )
            }
        }
    }
}

@Composable
fun TabsUi(){
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = sallonColor,
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            modifier = Modifier
                .clip(
                    RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                )
                .background(if (selectedTabIndex == 0) purple_200 else Color.White)

        ) {
            Text(
                "Chat",
                color = if (selectedTabIndex == 0) Color.Black else Color.Gray,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        Tab(
            selected = selectedTabIndex == 1,
            onClick = { selectedTabIndex = 1 },
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(if (selectedTabIndex == 1) purple_200 else Color.White)

        ) {
            Text(
                "Call",
                color = if (selectedTabIndex == 1) Color.Black else Color.Gray,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}