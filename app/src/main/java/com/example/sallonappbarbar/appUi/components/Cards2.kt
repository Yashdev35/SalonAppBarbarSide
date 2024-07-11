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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun OrderCard(
    imageUrl: String,
    orderType: List<String>,
    timeSlot: List<String>,
    phoneNumber: String,
    customerName: String,
    paymentMethod: String = "Cash",
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    accepted: Boolean = false
) {
    val showInfoDialog = rememberMaterialDialogState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
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
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
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
                                text = customerName,
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
                            text = "Order: ${orderType.joinToString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Time Slot: ${timeSlot.joinToString()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Payment Method: $paymentMethod",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
                if(!accepted){
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
                }else {
                    Text(
                        text = "Accepted",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                MaterialDialog(
                    dialogState = showInfoDialog,
                    buttons = {
                        positiveButton(text = "OK") { showInfoDialog.hide() }
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                    Text("Customer Phone Number: $phoneNumber")
                        Text(text ="Customer Name: $customerName")
                        Text(text = "Order Type: ${orderType.joinToString()}")
                }
                }
        }
        }
    }
}

@Composable
fun PendingNoCard(pendingOrderToday: MutableState<Int>) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(100.dp),
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row {
                Text(
                    text = "Today's Pending Orders: ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                )
                Text(
                    text = pendingOrderToday.value.toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}