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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    orderViewModel: OrderViewModel ,
) {
    BackHandler {
        barberDataViewModel.navigationItem.value= NavigationItem.Home
    }
    val reviewList by orderViewModel.reviewList.collectAsState()
    DoubleCard(
        midCarBody = {
            ReviewMidCard(
                rating =
                    reviewList.map { it.review.rating }.average()
            )
        },
        mainScreen = {
            ReviewList(orderViewModel)
        },
        topAppBar = {
            Text(
                text = "Review",
                modifier = Modifier
                    .padding(40.dp, 16.dp),
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
    orderViewModel: OrderViewModel
) {
    val scrollState = rememberScrollState()
    val reviewList by orderViewModel.reviewList.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            reviewList.forEach { review ->
                ReviewCard(
                    review = review.review
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 30.dp)
                )
            }
        }
    }
}

@Composable
fun ReviewMidCard(
    rating: Double,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Average Rating",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(16.dp)
        )
        RatingBar(onRatingChanged = { /*Do nothing*/ }, rating = rating)
    }
}
