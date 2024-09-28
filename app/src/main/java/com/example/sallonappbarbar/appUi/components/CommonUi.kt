package com.practicecoding.sallonapp.appui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.Star

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.components.ReviewCard
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import kotlinx.coroutines.delay

@Composable
fun CommonDialog(text:String="Loading") {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(horizontal = 20.dp)
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
                Text(
                    text = text,
                    modifier = Modifier,
                    fontSize = 24.sp,
                    color = sallonColor
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
@Composable
fun SuccessfulDialog() {
    val showDialog= remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = {showDialog.value=false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            restartOnPlay = true,
            iterations = 1,  // Play once
            speed = 1.0f
        )
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .size(300.dp, 300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Slots update Successfully!", color = sallonColor, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top=8.dp))
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(width = 300.dp, height = 130.dp)
                    ,
                    alignment = Alignment.Center
                )

                    Button(
                        onClick = {showDialog.value=false},
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, sallonColor, RoundedCornerShape(8.dp))

                        ,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "OK", color = sallonColor, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                    }

            }
        }
    }
}

@Composable
fun LaunchPhotoPicker(singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {

    singlePhotoPickerLauncher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )

}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = sallonColor,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
        color = Color.White,
        tonalElevation = 20.dp,
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val circles = listOf(
                remember { Animatable(initialValue = 0f) },
                remember { Animatable(initialValue = 0f) },
                remember { Animatable(initialValue = 0f) }
            )

            circles.forEachIndexed { index, animatable ->

                LaunchedEffect(key1 = animatable) {
                    delay(index * 100L)
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = keyframes {
                                durationMillis = 1200
                                0.0f at 0 using LinearOutSlowInEasing
                                1.0f at 300 using LinearOutSlowInEasing
                                0.0f at 600 using LinearOutSlowInEasing
                                0.0f at 1200 using LinearOutSlowInEasing
                            },
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }

            val circleValues = circles.map { it.value }
            val distance = with(LocalDensity.current) { travelDistance.toPx() }

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(spaceBetween)
            ) {
                circleValues.forEach { value ->
                    Box(
                        modifier = Modifier
                            .size(circleSize)
                            .graphicsLayer {
                                translationY = -value * distance
                            }
                            .background(
                                color = circleColor,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

    }
}

@Composable
fun CircularProgressWithAppLogo() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
        var isPlaying by remember { mutableStateOf(true) }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying, restartOnPlay = true, iterations = 10, speed = 0.75f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LottieAnimation(
                    composition = composition, progress = { progress }, modifier = Modifier
                        .size(250.dp)
                        .padding(start = 100.dp, top = 40.dp), alignment = Alignment.Center
                )
            }
        }
    }
}

@Composable
fun PasswordFields() {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        // Password Field
        androidx.compose.material.OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { androidx.compose.material.Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    androidx.compose.material.Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
        // Confirm Password Field
        androidx.compose.material.OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { androidx.compose.material.Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    androidx.compose.material.Icon(
                        imageVector = if (isPasswordVisible) Icons.Outlined.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
    }
}

@Composable
fun GoogleAndFacebook() {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_google),
            contentDescription = "Google",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    Toast
                        .makeText(context, "Google", Toast.LENGTH_SHORT)
                        .show()
                }
        )
        Spacer(modifier = Modifier.size(50.dp))
        Image(
            painter = painterResource(id = R.drawable.icon_facebook),
            contentDescription = "Facebook",
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    Toast
                        .makeText(context, "Facebook", Toast.LENGTH_SHORT)
                        .show()
                }
        )
    }
}

@Composable
fun BackButtonTopAppBar(
    onBackClick: () -> Unit,
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .padding(20.dp)
                .wrapContentSize(align = Alignment.BottomEnd)
                .clip(RoundedCornerShape(20.dp))
                .size(width = 40.dp, height = 40.dp),
            color = MaterialTheme.colorScheme.primary
        ) {

            androidx.compose.material.IconButton(
                onClick = {
                    onBackClick()

                },
                modifier = Modifier.background(color = Color.White)
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Next",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        androidx.compose.material.Text(
            text = title,
            modifier = Modifier
                .padding(40.dp, 26.dp),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    onRatingChanged: (Double) -> Unit,
    starsColor: Color = Color.Yellow
) {

    var isHalfStar = (rating % 1) != 0.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (index in 1..stars) {
            Icon(
                imageVector =
                if (index <= rating) {
                    Icons.Rounded.Star
                } else {
                    if (isHalfStar) {
                        isHalfStar = false
                        Icons.Rounded.Star
                    } else {
                        Icons.Rounded.Star
                    }
                },
                contentDescription = null,
                tint = starsColor,
                modifier = modifier
                    .clickable { onRatingChanged(index.toDouble()) }
            )
        }
        Text(
            text = rating.toString(),
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdvancedSignUpScreenPreview() {
    ReviewCard(imageUri = "", reviewRating = 4.0, username = "Adolf Hitler", reviewText = "Gas the jews", orderID = "sd")
}
