package com.example.sallonappbarbar.appUi.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.Screens.MainScreens.HomeScreen
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.HeadingText
import com.example.sallonappbarbar.appUi.Screens.initiators.AdvancedSignUpScreen
import com.example.sallonappbarbar.appUi.Screens.initiators.PriceSelector
import com.example.sallonappbarbar.appUi.Screens.initiators.ServiceSelectorScreen
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.aService
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
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
        composable(
            Screenes.OnBoardingScreenes.route,
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
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "Sign up to access all the feature of barber shop") },
                mainScreen = {
                    PhoneNumberScreen(activity = context as Activity,
                        navigateToVerification = { phoneNumber ->
                            navController.navigate(Screenes.OTPVerification.route + "/$phoneNumber")
                        }
                    )
                },
                topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "Sign up"
                    )
                }

            )
        }
        composable(Screenes.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "000"
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "We have sent an otp to $phoneNumber") },
                mainScreen = {
                    OtpVerificationScreen(
                        phoneNumber = phoneNumber,
                        activity = context as Activity,
                        navController = navController,
                    )
                },
                topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "OTP Verification"
                    )
                }
            )
        }
        composable(Screenes.BarbarsSignUp.route + "/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: "000"
            DoubleCard(
                midCarBody = { HeadingText(bodyText = "Enter your details to access all the feature of barber shop") },
                mainScreen = {
                    AdvancedSignUpScreen(
                        phoneNumber = phoneNumber,
                        activity = context as Activity,
                        navController=navController
                    )
                },
                topAppBar = {
                    BackButtonTopAppBar(
                        onBackClick = { navController.popBackStack() },
                        title = "Sign Up"
                    )
                }
            )
        }
//        composable(Screenes.SignUp.route) {
//            DoubleCard(
//                midCarBody = { HeadingText(bodyText = "Enter your details to access all the feature of barber shop") },
//                mainScreen = {
//                    AdvancedSignUpScreen(
//                        phoneNumber = "000",
//                        activity = context as Activity,
//                        navController = navController
//                    )
//                },
//                topAppBar = {
//                    BackButtonTopAppBar(
//                        onBackClick = { navController.popBackStack() },
//                        title = "Sign Up"
//                    )
//                }
//            )
//        }
        composable(Screenes.SelecterScr.route) {
            val barberData = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("BarberModel")?:BarberModel(
                "0",
                "name",
                "email",
                "password",
                "phone",
                "address",
                "city",
                "state",
                "zip",
                "country",
            )
            ServiceSelectorScreen( barberData,navController)
        }
        composable(Screenes.PriceSelector.route){
            val services = navController.previousBackStackEntry?.savedStateHandle?.get<List<aService>>("services")?: emptyList()
            PriceSelector(
                navController = navController,
                aServices = services,
                activity = context as Activity
            )
        }
        composable(Screenes.Home.route){
            HomeScreen(activity = context as Activity, navController = navController)
        }
    }
}