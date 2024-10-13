package com.example.sallonappbarbar.appUi.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor

@Composable
fun AlertDialogBox(
    title: String,
    message: String,
    onConfirmButton: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    onDismissButton: @Composable () -> Unit
) {

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = {
            onDismissRequest()
        },
        title = { SaloonColorText(title) },
        text = { SaloonColorText(text = message, textSize = 14) },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        confirmButton = {
            onConfirmButton()
        },
        dismissButton = {
            onDismissButton()

        }
    )
}


@Composable
fun CommonDialog(text:String="Loading") {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 40.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .border(2.dp, sallonColor, RoundedCornerShape(10.dp))
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                SaloonColorText(
                    text = text,
                )
                Spacer(modifier = Modifier.width(40.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(50.dp),
                    color = Color(sallonColor.toArgb()),
                    trackColor = Color(purple_200.toArgb()),
                )
            }
        }
    }
}

