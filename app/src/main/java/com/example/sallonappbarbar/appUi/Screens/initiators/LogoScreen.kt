package com.practicecoding.sallonapp.screens.initiatorScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screenes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun LogoScreen(
    navController: NavController,
    logoDurationMillis: Long = 2000L // Default duration of 2000 milliseconds (2 seconds)
){

    LaunchedEffect(true) {
        delay(logoDurationMillis)
        val updatedCurrentUser = FirebaseAuth.getInstance().currentUser
        val dummyPhoneNumber = "1234567890"
        if (updatedCurrentUser == null) {
            navController.navigate(Screenes.OnBoardingScreenes.route){
                popUpTo(Screenes.Logo.route) {
                    inclusive = true
                }
            }
        }else{
            navController.navigate(Screenes.BarbarsSignUp.route+ "/$dummyPhoneNumber"){
                popUpTo(Screenes.Logo.route) {
                    inclusive = true
                }
            }

        }

    }
    Surface(
        modifier = Modifier.fillMaxSize(),
            ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                color = colorResource(id = R.color.sallon_color)
            ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.salon_app_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .aspectRatio(1.0f).wrapContentSize().clip(shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoScreenPreview() {
    LogoScreen(navController = rememberNavController())
}
