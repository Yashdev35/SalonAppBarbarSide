package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.app.Activity
import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.components.BottomAppNavigationBar
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.components.OrderCard
import com.example.sallonappbarbar.appUi.components.PendingNoCard
import com.example.sallonappbarbar.appUi.components.ProfileWithNotification
import com.example.sallonappbarbar.appUi.components.ShimmerEffectBarber
import com.example.sallonappbarbar.appUi.components.ShimmerEffectMainScreen
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.OrderEvent
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.TimeSlot
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.CircularProgressWithAppLogo
import kotlinx.coroutines.launch

@Composable
fun MainScreen1(
    viewModel: BarberDataViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel = hiltViewModel(),
    navHostController: NavController,
    context: Context,
) {
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
                NavigationItem.Home -> if (orderViewModel.isLoading.value) {
                    ShimmerEffectBarber()
                } else {
                    TopScreen(navHostController, context, orderViewModel)
                }
                NavigationItem.Book -> ScheduleScreen(navController = navHostController)
                NavigationItem.Message -> MessageScreen(navHostController) // Placeholder for MessageScreen
                NavigationItem.Profile -> ProfileScreen() // Placeholder for ProfileScreen
                NavigationItem.Review -> androidx.compose.material3.Text("Review Screen")  // Placeholder for ReviewScreen
            }
        }
    }
}

@Composable
fun TopScreen(
    navController: NavController,
    context: Context,
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    DoubleCard(
        midCarBody = {
            PendingNoCard(pendingOrderToday =orderViewModel.todayOrderNo)
        },
        mainScreen = {
            HomeScreen(
                activity = context as Activity,
                navController = navController,
                pendingOrders = orderViewModel.pendingOrderList.value.toMutableList(),
                acceptedOrders = orderViewModel.acceptedOrderList.value.toMutableList()
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
fun OrderList(orders: List<OrderModel>, isCompleted: Boolean,
              orderViewModel: OrderViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderCard(
                imageUrl = order.imageUrl,
                orderType = order.orderType,
                timeSlot = order.timeSlot,
                phoneNumber = order.phoneNumber,
                customerName = order.customerName,
                onAccept = {
                    order.orderStatus = OrderStatus.ACCEPTED
                    scope.launch{
                        orderViewModel.updateOrderStatus(order.orderId, OrderStatus.ACCEPTED.status)
                    } },
                onDecline = {
                    order.orderStatus = OrderStatus.DECLINED
                    scope.launch{
                        orderViewModel.updateOrderStatus(order.orderId, OrderStatus.DECLINED.status)
                    } },
                accepted = isCompleted
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavController,
    viewModel: BarberDataViewModel = hiltViewModel(),
    pendingOrders: MutableList<OrderModel>,
    acceptedOrders: MutableList<OrderModel>
) {
    val context = LocalContext.current
    var isBarberShopOpen by remember { mutableStateOf(false) }
    var isOpenOrClose by remember { mutableStateOf("Open") }
    var isLoading by remember { mutableStateOf(false) }
    val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
    if (isLoading) {
        CircularProgressWithAppLogo()
    }

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.getBarberData(activity).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        isLoading = false
                        Toast.makeText(activity, "Data Loaded", Toast.LENGTH_SHORT).show()
                        val barberData = resource.result
                        isBarberShopOpen = barberData.open!!
                        isOpenOrClose = if (isBarberShopOpen) "Open" else "Close"
                    }
                    is Resource.Failure -> {
                        isLoading = false
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
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.White
            ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage,
                 containerColor = Color(sallonColor.toArgb()),
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 20.dp,topEnd = 20.dp))
                ) {
                Tab(
                    text = { Text("Pending Orders",color = Color.White)
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
                    text = { Text("Accepted Orders", color = Color.White) },
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                )
            }

            HorizontalPager(state = pagerState, modifier = Modifier
                .height((screenHeight).dp)
                .background(Color.White)
                .border(
                    border = BorderStroke(2.dp, Color(sallonColor.toArgb())),
                )) { page ->
                when (page) {
                    0 -> OrderList(orders = pendingOrders, isCompleted = false)
                    1 -> OrderList(orders = acceptedOrders, isCompleted = true)
                }
            }
        }
    }
}