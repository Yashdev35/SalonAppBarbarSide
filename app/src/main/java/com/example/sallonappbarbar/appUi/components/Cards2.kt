package com.example.sallonappbarbar.appUi.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.data.model.OrderModel
import com.example.sallonappbarbar.data.model.ReviewModel
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.RatingBar
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrderCard(
    order:OrderModel,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onComplete: () -> Unit,
    accepted: Boolean = false
) {
    val showInfoDialog = rememberMaterialDialogState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val totalTime = order.listOfService.sumOf { it.time.toInt() * it.count }
    val totalPrice = order.listOfService.sumOf { it.price.toInt() * it.count }
    Card(
        shape = RoundedCornerShape(12),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = CardColors(
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White,
            containerColor = Color.White
        ),
        border = BorderStroke(1.2.dp, Color(sallonColor.toArgb()))
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = order.userImageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.FillBounds
                    )
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.userName,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = {
                                    showInfoDialog.show()
                                },
                                modifier = Modifier
                                    .zIndex(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(sallonColor.toArgb())
                                )
                            }
                        }
                        Text(
                            text = "Order: ${order.listOfService.joinToString { it.serviceName }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Date: ${order.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Time Slot: ${order.timeSlot.joinToString { it.time }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Payment Method: ${order.paymentMethod}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                if (order.orderStatus==OrderStatus.PENDING) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(Purple80.toArgb())
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Accept", color = Color.Black)
                        }
                        Button(
                            onClick = onDecline,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(Purple80.toArgb())
                            )
                        ) {
                            Text("Decline", color = Color.Black)
                        }
                    }
                } else if(order.orderStatus==OrderStatus.ACCEPTED) {
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(Purple80.toArgb())
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Completed", color = Color.Black)
                    }
                }else if(order.orderStatus==OrderStatus.CANCELLED){
                    Text(
                        "Cancelled", color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }else{
                    if(order.review.reviewTime.isNotEmpty()&&order.review.reviewText.toString().isNotEmpty()){
                        ReviewText(order.review)
                    }else {
                        Text(
                            "No review yet!", color = sallonColor,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                MaterialDialog(
                    dialogState = showInfoDialog,
                    buttons = {
                        positiveButton(text = "OK") { showInfoDialog.hide() }
                    },
                    border = BorderStroke(1.dp, sallonColor),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                androidx.compose.material3.Text(
                                    text = "Date:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,

                                    )
                                androidx.compose.material3.Text(
                                    text = order.date,
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Time Slots:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,

                                    )

                                Text(
                                    text = order.timeSlot.joinToString { it.time },
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                            }

                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Card(
                            colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Gender Type:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${if (order.genderCounter[0] > 0) "Male  " else ""}${if (order.genderCounter[1] > 0) "Female  " else ""}${if (order.genderCounter[2] > 0) "Other" else ""}",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        ExpandableCard(title = "Service List", expanded = true) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                order.listOfService.forEach { service ->

                                    ServiceNameAndPriceCard(
                                        serviceName = service.serviceName,
                                        serviceTime = service.time,
                                        servicePrice = service.price,
                                        count = service.count
                                    )

                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(5.dp),
                        )
                        Card(
                            colors = CardColors(Color.White, Color.White, Color.White, Color.White),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Total Time:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)


                                )
                                Text(
                                    text = "$totalTime Minutes",
                                    color = Color.Black,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Total Price:",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "Rs.$totalPrice",
                                    color = sallonColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ReviewText(
    review: ReviewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        RatingBar(onRatingChanged = { }, rating = review.rating)
        Text(text = review.reviewText, color = sallonColor, fontSize = 16.sp)
    }
}
@Composable
fun PendingNoCard(pendingOrderToday: Int, acceptedOrderToday: Int) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(80.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = Color.White,
            disabledContentColor = Color.Black,
            disabledContainerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.salon_app_logo), // replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row {
                    Text(
                        text = "Today's Pending Orders: ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                        fontSize = 16.sp
                    )
                    Text(
                        text = pendingOrderToday.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ), fontSize = 20.sp

                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        text = "Today's Accepted Orders: ",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top=0.5.dp)

                    )
                    Text(
                        text = acceptedOrderToday.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ), fontSize = 20.sp,


                    )
                }
            }

        }
    }
}

@Composable
fun ReviewCard(
    review : ReviewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        val currentTime = LocalDateTime.now()
        val reviewTime =
            LocalDateTime.parse(review.reviewTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val duration = Duration.between(reviewTime, currentTime)
        val timePassed = when {
            duration.toDays() > 0 -> "${duration.toDays()} days ago"
            duration.toHours() > 0 -> "${duration.toHours()} hours ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
            else -> "Just now"}
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.LightGray,
                    modifier = Modifier
                        .size(50.dp)
                ) {
                    /*TODO profile picture*/
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row {
                        Text(
                            text = review.userName,
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Text(
                            text = timePassed,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        repeat((Math. round(review.rating * 10.0) / 10.0).toInt()) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star Icon",
                                tint = Color.Yellow,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Text(
                text = review.reviewText,
                fontSize = 16.sp,
                maxLines = 3,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
            )
        }

    }
}

//@Preview
//@Composable
//fun OrderCardPreview() {
//    OrderCard(
//        imageUrl = "",
//        orderType = listOf("Haircut"),
//        date = "2021-09-12",
//        timeSlot = listOf("10:00 AM - 11:00 AM"),
//        phoneNumber = "1234567890",
//        customerName = "John Doe",
//        onAccept = { /*TODO*/ },
//        onDecline = { /*TODO*/ },
//        onComplete = { /*TODO*/ },
//        accepted = false,
//        paymentMethod = "Cash"
//    )
//}