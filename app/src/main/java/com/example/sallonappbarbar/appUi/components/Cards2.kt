package com.example.sallonappbarbar.appUi.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun OrderCard(
    imageUrl: String,
    orderType: String,
    timeSlot: String,
    phoneNumber: String,
    customerName: String,
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
                            modifier = Modifier.fillMaxWidth(),
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
                            text = "Order: $orderType",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "Time Slot: $timeSlot",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
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
                        Text(text = "Order Type: $orderType")
                }
                }
        }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderCardPreview() {
    OrderCard(
        imageUrl = "https://images.unsplash.com/photo-1622838320000-4b3b3b3b3b3b",
        orderType = "Haircut",
        timeSlot = "10:00 AM - 11:00 AM",
        phoneNumber = "9373182023",
        customerName = "John Doe",
        onAccept = {},
        onDecline = {}
    )
}