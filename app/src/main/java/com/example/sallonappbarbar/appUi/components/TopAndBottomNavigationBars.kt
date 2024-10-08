package com.example.sallonappbarbar.appUi.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height


enum class NavigationItem(var icon: ImageVector, val iconName: String) {
    Home(Icons.Default.Home, "Home"),
    Book(Icons.AutoMirrored.TwoTone.List, "Booking"),
    Message(Icons.AutoMirrored.Filled.Send, "Chats"),
    Review(Icons.Default.Favorite, "Review"),
    Profile(Icons.Default.Person, "Profile")
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

@Composable
fun BottomAppNavigationBar(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    messageCount: Int = 0
) {
    val bottomBarItems = NavigationItem.entries.toTypedArray()
    AnimatedNavigationBar(
        modifier = Modifier
            .height(65.dp)
            .background(Color.White)
//            .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)).border(0.5.dp, color = Color.LightGray)
            .fillMaxWidth(),
        selectedIndex = selectedItem.ordinal,
        barColor = sallonColor,
        ballAnimation = Parabolic(tween(durationMillis = 300)),
        ballColor = sallonColor,
//        cornerRadius = shapeCornerRadius(36.dp),
        indentAnimation = Height(tween(durationMillis = 300)),
    ) {
        bottomBarItems.forEach { item ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable { onItemSelected(item) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box{
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(42.dp)
                                .padding(top = 10.dp),
                            tint = if (selectedItem == item) Color.White else Color.Gray
                        )
                        if (item == NavigationItem.Message && messageCount > 0) {
                            CircleWithMessageCount(messageCount = messageCount)
                        }
                    }
                    Text(
                        text = item.iconName, modifier = Modifier,
                        color = if (selectedItem == item) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun CircleWithMessageCount(
    messageCount: Int
) {
    Box(
        modifier = Modifier
            .size(20.dp) // Adjust size as needed
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (messageCount > 0) {
            Canvas(
                modifier = Modifier.size(50.dp).align(Alignment.TopEnd) // Ensure the canvas size matches the Box
            ) {
                drawCircle(
                    color = sallonColor, // Change color as needed
                    radius = 6.5.dp.toPx(), // Adjust radius to fit within the Box
                    center = Offset(size.width / 2, size.height / 2) // Center the circle
                )
            }
        }
    }
}

///*Top App Bars*/

@Composable
fun TransparentTopAppBar(
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(36.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                    )
                }
            }
            Row {
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = Color(sallonColor.toArgb()),
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircleWithMessageCountPreview(){
BottomAppNavigationBar(selectedItem = NavigationItem.Home, onItemSelected = {}, messageCount = 5)
}