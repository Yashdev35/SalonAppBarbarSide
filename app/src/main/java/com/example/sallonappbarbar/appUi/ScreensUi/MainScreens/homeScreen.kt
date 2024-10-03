package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.components.BottomAppNavigationBar
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.components.OrderCard
import com.example.sallonappbarbar.appUi.components.PendingNoCard
import com.example.sallonappbarbar.appUi.components.ProfileWithNotification
import com.example.sallonappbarbar.appUi.components.ShimmerEffectBarber
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.MessageViewModel
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration

@Composable
fun MainScreen1(
    orderViewModel: OrderViewModel = hiltViewModel(),
    messageViewModel: MessageViewModel = hiltViewModel(),
    navHostController: NavController,
    barberDataViewModel: GetBarberDataViewModel = hiltViewModel(),
    context: Context,
) {
//    var selectedScreen by remember { mutableStateOf(barberDataViewModel.navigationItem.value) }
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
                    if (orderViewModel.isLoading.value) {
                        ShimmerEffectBarber()
                    } else {
                        TopScreen(
                            orderViewModel,
                        )
                    }
                }

                NavigationItem.Book -> {
                    barberDataViewModel.navigationItem.value=NavigationItem.Book
                    ScheduleScreen(navController = navHostController,barberDataViewModel)
                }
                NavigationItem.Message -> {
                    barberDataViewModel.navigationItem.value=NavigationItem.Message
                    MessageScreen(navHostController, messageViewModel,barberDataViewModel)
                }
                NavigationItem.Profile -> {
                    barberDataViewModel.navigationItem.value=NavigationItem.Profile
                    ProfileScreen(navHostController,barberDataViewModel,orderViewModel)
                }
                NavigationItem.Review -> {
                    barberDataViewModel.navigationItem.value=NavigationItem.Review
                    ReviewScreen(barberDataViewModel,orderViewModel)
                }
            }
        }
    }
}

@Composable
fun TopScreen(
    orderViewModel: OrderViewModel,
) {
val todayPendingOrderNo by orderViewModel.todayPendingOrderNo.collectAsState()
val todayAcceptedOrderNo by orderViewModel.todayAcceptedOrderNo.collectAsState()
    DoubleCard(
        midCarBody = {
            PendingNoCard(pendingOrderToday = todayPendingOrderNo,
                acceptedOrderToday =todayAcceptedOrderNo)
        },
        mainScreen = {
            HomeScreen(
                orderViewModel = orderViewModel
            )
        },
        topAppBar = {
            ProfileWithNotification(
                onProfileClick = { /*TODO*/ },
                onNotificationClick = { /*TODO*/ })
        }
    )
}

@Composable
fun OrderList(
    orders: List<OrderModel>, isAccepted: Boolean,
    orderViewModel: OrderViewModel
) {

    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderCard(
                order=order,
                onAccept = {
                    order.orderStatus = OrderStatus.ACCEPTED
                    scope.launch {
                        orderViewModel.updateOrderStatus(order, OrderStatus.ACCEPTED.status)
                    }
                },
                onDecline = {
                    order.orderStatus = OrderStatus.CANCELLED
                    scope.launch {
                        orderViewModel.updateOrderStatus(order, OrderStatus.CANCELLED.status)
                    }
                },
                accepted = isAccepted,
                onComplete = {
                    scope.launch {
                        orderViewModel.updateOrderStatus(
                            order,
                            OrderStatus.COMPLETED.status
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    orderViewModel: OrderViewModel
) {
    if(orderViewModel.isUpdating.value){
        CommonDialog("Updating")
    }
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
                    0 -> OrderList(orders = pendingOrders, isAccepted = false,orderViewModel)
                    1 -> OrderList(orders = acceptedOrders, isAccepted = true,orderViewModel)
                    2-> OrderList(orders = cancelledOrders, isAccepted = false, orderViewModel = orderViewModel)
                }
            }
        }
    }
}