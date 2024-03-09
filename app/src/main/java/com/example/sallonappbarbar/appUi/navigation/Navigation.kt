package com.example.sallonappbarbar.appUi.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.Screens.DoubleCard
import com.example.sallonappbarbar.appUi.Screens.HeadingText
import com.example.sallonappbarbar.appUi.Screens.initiators.AdvancedSignUpScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OtpVerificationScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.PhoneNumberScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Screenes.Logo.route) {
        composable(Screenes.Logo.route) {
            LogoScreen(navController = navController)
        }
        composable(Screenes.OnBoardingScreenes.route,
        ) {
            val imageList = listOf(
                R.drawable.onboarding1,
                R.drawable.onboarding2,
                R.drawable.onboarding3
            )
            val onBoardingTextList = listOf(
                OnBoardingPageText(
                    mainHeading = "Heading 1",
                    bodyText = "Body 1"
                ),
                OnBoardingPageText(
                    mainHeading = "Heading 2",
                    bodyText = "Body 2"
                ),
                OnBoardingPageText(
                    mainHeading = "Heading 3",
                    bodyText = "Body 3"
                ),
            )
            OnBoardingScreen(
                navController = navController,
                imageList = imageList,
                OnBoardingTextList = onBoardingTextList
            )
        }
        composable(Screenes.PhoneNumberScreen.route) {
            DoubleCard(title = "Sign up",{
                navController.popBackStack()
            }, body = {
                HeadingText(bodyText = "Sign up to access all the feature of barber shop")
            }) {
                PhoneNumberScreen(activity =context as Activity,
                    navigateToVerification = {phoneNumber ->
                        navController.navigate(Screenes.OTPVerification.route + "/$phoneNumber")
                    }
                )
            }
//            DoubleCard(title = "Sign up", body = {
//                HeadingText(bodyText = "Sign up to access all the feature of barber shop")
//            }) {
//                PhoneNumberScreen(activity =context as Activity,
//                    navigateToVerification = {phoneNumber ->
//                        navController.navigate(Screens.OTPVerification.route + "/$phoneNumber")
//                    }
//                )
//            }
        }
        composable(Screenes.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")?:"000"
            DoubleCard(title = "OTP verification",{
                navController.popBackStack()
            }, body =
            {
                HeadingText(bodyText = "We've send the code to your phone number $phoneNumber")
            }
            ) {
                OtpVerificationScreen(phoneNumber= phoneNumber,activity = context as Activity,
                    navController
                )
            }
        }
        composable(Screenes.BarbarsSignUp.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")?:"000"
            DoubleCard(title = "Sign Up",{
                navController.popBackStack()
            }, body = {
                HeadingText(bodyText = "Enter your details to access all the feature of barber shop")
            }) {
                AdvancedSignUpScreen(phoneNumber = phoneNumber)
            }
        }
    }
}