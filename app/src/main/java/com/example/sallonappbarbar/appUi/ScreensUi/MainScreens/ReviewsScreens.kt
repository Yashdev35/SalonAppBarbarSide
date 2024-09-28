package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.NavigationItem
import com.example.sallonappbarbar.appUi.components.ReviewCard
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel
import com.practicecoding.sallonapp.appui.components.RatingBar

@Composable
fun ReviewScreen(
    barberDataViewModel: GetBarberDataViewModel,
    orderViewModel: OrderViewModel = hiltViewModel(),
) {
    BackHandler {
        barberDataViewModel.navigationItem.value= NavigationItem.Home
    }
    DoubleCard(
        midCarBody = {
            ReviewMidCard(
                rating = remember {
                    mutableDoubleStateOf(orderViewModel.averageRating.value)
                }
            )
        },
        mainScreen = {
            ReviewList(orderViewModel)
        },
        topAppBar = {
            Text(
                text = "Review",
                modifier = Modifier
                    .padding(40.dp, 26.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    )
}

@Composable
fun ReviewList(
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            orderViewModel.reviewList.value.forEach { review ->
                ReviewCard(
                    imageUri = review.userDp,
                    reviewRating = review.rating,
                    username = review.userName,
                    reviewText = review.reviewText,
                    orderID = review.orderId
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun ReviewMidCard(
    rating: MutableState<Double>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Average Rating",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        RatingBar(onRatingChanged = { /*Do nothing*/ }, rating = rating.value)
    }
}
