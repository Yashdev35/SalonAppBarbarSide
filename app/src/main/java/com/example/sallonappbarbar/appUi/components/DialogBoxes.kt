package com.example.sallonappbarbar.appUi.components

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

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