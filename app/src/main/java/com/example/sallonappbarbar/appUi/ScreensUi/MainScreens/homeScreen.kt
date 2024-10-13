package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sallonappbarbar.appUi.components.CommonDialog
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.OrderList
import com.example.sallonappbarbar.appUi.components.PendingNoCard
import com.example.sallonappbarbar.appUi.components.Profile
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel
import com.example.sallonappbarbar.ui.theme.sallonColor
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    orderViewModel: OrderViewModel,
) {
    val todayPendingOrderNo by orderViewModel.todayPendingOrderNo.collectAsState()
    val todayAcceptedOrderNo by orderViewModel.todayAcceptedOrderNo.collectAsState()
    DoubleCard(
        midCarBody = {
            PendingNoCard(
                pendingOrderToday = todayPendingOrderNo,
                acceptedOrderToday = todayAcceptedOrderNo
            )
        },
        mainScreen = {
            OrderStatusScreen(
                orderViewModel = orderViewModel
            )
        },
        topAppBar = {
            Profile(
                onProfileClick = { /*TODO*/ },
                onNotificationClick = { /*TODO*/ })
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OrderStatusScreen(
    orderViewModel: OrderViewModel
) {
    val pendingOrders by orderViewModel.pendingOrderList.collectAsState()
    val acceptedOrders by orderViewModel.acceptedOrderList.collectAsState()
    val cancelledOrders by orderViewModel.cancelledOrderList.collectAsState()
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(sallonColor.toArgb()),
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                Tab(
                    text = {
                        Text("Pending", color = Color.White)
                    },
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    modifier = Modifier.clip(CircleShape)
                )
                Tab(
                    text = { Text("Accepted", color = Color.White) },
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                )
                Tab(
                    text = { Text("Cancelled", color = Color.White) },
                    selected = pagerState.currentPage == 2,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    },
                )
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .height((screenHeight).dp)
                    .background(Color.White)
                    .border(
                        border = BorderStroke(2.dp, Color(sallonColor.toArgb())),
                    )
            ) { page ->
                when (page) {
                    0 -> OrderList(orders = pendingOrders, isAccepted = false, orderViewModel)
                    1 -> OrderList(orders = acceptedOrders, isAccepted = true, orderViewModel)
                    2 -> OrderList(
                        orders = cancelledOrders,
                        isAccepted = false,
                        orderViewModel = orderViewModel
                    )
                }
            }
        }
    }
}