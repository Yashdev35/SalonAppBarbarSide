package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.sallonColor

@Composable
fun ProfileScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(Purple40.toArgb()))
            .padding(16.dp)
    ) {
        ProfileCard(
            imageUrl = "https://example.com/profile.jpg",
            name = "John Doe",
            address = "123 Barber St, Haircut City",
            rating = 4.5f
        )

        Divider(color = Color.Black, thickness = 1.dp)

        SettingsCard(
            title = "Account Settings",
            onClick = { Toast.makeText(context, "Account Settings", Toast.LENGTH_SHORT).show() }
        )

        Divider(color = Color.Black, thickness = 1.dp)

        SettingsCard(
            title = "Notification Settings",
            onClick = { Toast.makeText(context, "Notification Settings", Toast.LENGTH_SHORT).show() }
        )

        Divider(color = Color.Black, thickness = 1.dp)

        SettingsCard(
            title = "Privacy Settings",
            onClick = { Toast.makeText(context, "Privacy Settings", Toast.LENGTH_SHORT).show() }
        )

        Divider(color = Color.Black, thickness = 1.dp)

        SettingsCard(
            title = "Log Out",
            onClick = { Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show() }
        )

        Divider(color = Color.Black, thickness = 1.dp)
    }
}

@Composable
fun ProfileCard(imageUrl: String, name: String, address: String, rating: Float) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(sallonColor.toArgb())
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating.toInt()) Color.Yellow else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "(${rating})",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCard(title: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(sallonColor.toArgb())
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}