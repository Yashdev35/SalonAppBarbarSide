package com.example.sallonappbarbar.appUi.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.viewModel.OrderStatus
import com.example.sallonappbarbar.appUi.viewModel.OrderViewModel
import com.example.sallonappbarbar.data.model.OrderModel
import kotlinx.coroutines.launch

@Composable
fun OrderList(
    orders: List<OrderModel>, isAccepted: Boolean,
    orderViewModel: OrderViewModel
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderCard(
                order = order,
                onAccept = {
//                    order.orderStatus = OrderStatus.ACCEPTED
                   val job= scope.launch {
                        orderViewModel.updateOrderStatus(order, OrderStatus.ACCEPTED.status)
                    }
                    job.invokeOnCompletion {
                        Toast.makeText(context,"Booking Status updated Successfully👍",Toast.LENGTH_SHORT).show()
                    }
                },
                onDecline = {
//                    order.orderStatus = OrderStatus.CANCELLED
                    val job= scope.launch {
                        orderViewModel.updateOrderStatus(order, OrderStatus.CANCELLED.status)
                    }
                    job.invokeOnCompletion {
                        Toast.makeText(context,"Booking Status updated Successfully👍",Toast.LENGTH_SHORT).show()
                    }
                },
                accepted = isAccepted,
                onComplete = {
                    val job=   scope.launch {
                        orderViewModel.updateOrderStatus(
                            order,
                            OrderStatus.COMPLETED.status
                        )
                    }
                    job.invokeOnCompletion {
                        Toast.makeText(context,"Booking Status updated Successfully👍",Toast.LENGTH_SHORT).show()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
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