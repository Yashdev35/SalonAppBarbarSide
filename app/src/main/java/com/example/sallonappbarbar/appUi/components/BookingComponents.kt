package com.example.sallonappbarbar.appUi.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.Purple200Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OrderList(
    ordersList: List<OrderModel>, isAccepted: Boolean,
    orderViewModel: OrderViewModel
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showAlertDialogBox = remember { mutableStateOf(false) }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var message by remember {
        mutableStateOf("")
    }
    var showLoadingBox by remember {
        mutableStateOf(false)
    }
    if (showLoadingBox){
        LoadingAnimation(text = "Updating...")
    }

    if (showAlertDialogBox.value) {
        AlertDialogBox(
            title = "Want to send whatsapp message",
            message = "",
            onConfirmButton = {
                Purple200Button(text = "Yes", onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                String.format(
                                    "https://api.whatsapp.com/send?phone=%s&text=%s",
                                    "+91$phoneNumber",
                                    message
                                )
                            )
                        )
                    )
                    showAlertDialogBox.value=false
                })
            },
            onDismissButton = {
                Purple200Button(
                    text = "No",
                    onClick = { showAlertDialogBox.value = false })
            }, onDismissRequest = { showAlertDialogBox.value = false })
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(ordersList.size) { index ->
            val order = ordersList[index]
            OrderCard(
                order = order,
                onAccept = {
                    val job = scope.launch(Dispatchers.Main) {
                        orderViewModel.updateOrderStatus(order, OrderStatus.ACCEPTED.status)
                        showLoadingBox = true
                        delay(1000)
                    }
                    job.invokeOnCompletion {
                        showLoadingBox = false
                        phoneNumber = order.userPhoneNumber
                        message = "Hello, I am accepting your booking.\n" +
                                "Thank you!"
                        showAlertDialogBox.value = true
                        sendSms(
                            order = order,
                            context,
                            message = "Hello, I am accepting your booking.\n" +
                                    "Thank you!"
                        )
                        Toast.makeText(
                            context,
                            "Booking Status updated Successfully👍",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onDecline = {
                    val job = scope.launch(Dispatchers.Main) {
                        orderViewModel.updateOrderStatus(order, OrderStatus.CANCELLED.status)
                        showLoadingBox = true
                        delay(1000)
                    }
                    job.invokeOnCompletion {
                        showLoadingBox = false
                        phoneNumber = order.userPhoneNumber
                        message = "Hello, I am cancelling your booking.\n" +
                                "Sorry for any inconvenience caused.\n" +
                                "Thank you!"
                        showAlertDialogBox.value = true
                        sendSms(
                            order = order,
                            context,
                            message = "Hello, I am cancelling your booking.\n" +
                                    "Sorry for any inconvenience caused.\n" +
                                    "Thank you!"
                        )
                        Toast.makeText(
                            context,
                            "Booking Status updated Successfully👍",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                },
                accepted = isAccepted,
                onComplete = {
                    val job = scope.launch(Dispatchers.Main) {
                        orderViewModel.updateOrderStatus(
                            order,
                            OrderStatus.COMPLETED.status
                        )
                        showLoadingBox = true
                        delay(1000)
                    }
                    job.invokeOnCompletion {
                        showLoadingBox = false
                        phoneNumber = order.userPhoneNumber
                        message = "Your booking has been completed."+"\nThank you!"
                        showAlertDialogBox.value = true
                        sendSms(
                            order = order,
                            context,
                            message ="Your booking has been completed."+"\nThank you!"
                        )
                        Toast.makeText(
                            context,
                            "Booking Status updated Successfully👍",
                            Toast.LENGTH_SHORT
                        ).show()
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
                        modifier = Modifier.padding(top = 0.5.dp)

                    )
                    Text(
                        text = acceptedOrderToday.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        fontSize = 20.sp,


                        )
                }
            }

        }
    }
}