package com.practicecoding.sallonapp.appui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sallonappbarbar.appUi.components.SaloonColorText
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor

@Composable
fun GeneralButton(text: String, width: Int, height: Int, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(width.dp)
            .height(80.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .background(color = Color(sallonColor.toArgb()), shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun Purple200Button(
    text: String,
    textSize: Int=16,
    width: Int = 90,
    height: Int = 40,
    onClick: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp)
) {
    Button(
        onClick = { onClick() },
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = purple_200,
        ), modifier = Modifier

    ) {
        SaloonColorText(text = text, textSize = textSize)
    }
}

@Preview(showBackground = false)
@Composable
fun Show() {
    Purple200Button(text = "Nimish", textSize = 10, onClick = { /*TODO*/ })
}


