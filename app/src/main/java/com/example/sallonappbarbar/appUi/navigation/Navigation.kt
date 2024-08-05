package com.example.sallonappbarbar.appUi.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.ChatScreen
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.MainScreen1
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.UpdateBarberInfoScreen
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.SlotAdderScreen
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.AdvancedSignUpScreen
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.PriceSelector
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.ServiceSelectorScreen
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.HeadingText
import com.example.sallonappbarbar.data.model.ServiceModel
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
    val enterTransition =
        slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )

    val exitTransition =
        slideOutHorizontally(
            targetOffsetX = { -1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val popEnterTransition =
        slideInHorizontally(
            initialOffsetX = { -1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        )

    val popExitTransition =
        slideOutHorizontally(
            targetOffsetX = { 1000 },
            animationSpec = spring(
                stiffness = Spring.StiffnessVeryLow,
                dampingRatio = Spring.DampingRatioNoBouncy
            )
        )

    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination =   Screens.Logo.route
    ){
            composable(Screens.Logo.route) {
                LogoScreen(navController = navController)
            }
            composable(
                Screens.OnBoardingScreenes.route,
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
            composable(Screens.PhoneNumberScreen.route) {
                DoubleCard(
                    midCarBody = { HeadingText(bodyText = "Sign up to access all the feature of barber shop") },
                    mainScreen = {
                        PhoneNumberScreen(activity = context as Activity,
                            navigateToVerification = { phoneNumber ->
                                navController.navigate(Screens.OTPVerification.route + "/$phoneNumber")
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
            composable(Screens.OTPVerification.route + "/{phoneNumber}") { backStackEntry ->
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
            composable(Screens.BarbarsSignUp.route ) {
                DoubleCard(
                    midCarBody = { HeadingText(bodyText = "Enter your details to access all the feature of barber shop") },
                    mainScreen = {
                        AdvancedSignUpScreen(

                            activity = context as Activity,
                            navController = navController
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

            composable(Screens.SelecterScr.route) {
                val isUpdatingService =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("isUpdatingService")
                        ?: false
                ServiceSelectorScreen( navController, isUpdatingService)
            }
            composable(Screens.PriceSelector.route) {
                val services =
                    navController.previousBackStackEntry?.savedStateHandle?.get<List<ServiceModel>>("services")
                        ?: emptyList()
                val isUpdatingServiceFSSScr =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("isUpdatingServiceFSSScr")
                        ?: false
                val isUpdatingService =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("isUpdatingService")
                        ?: false
                PriceSelector(
                    navController = navController,
                    services = services,
                    activity = context as Activity,
                    isUpdatingServiceFSSScr = isUpdatingServiceFSSScr,
                    isUpdatingService = isUpdatingService
                )
            }
            composable(Screens.Home.route,
                enterTransition = { enterTransition },
                exitTransition = { exitTransition },
                popEnterTransition = { popEnterTransition },
                popExitTransition = { popExitTransition }
            ) {
                MainScreen1(
                    navHostController = navController,
                    context = context
                )
            }
            composable(Screens.SlotAdderScr.route) {
                val isUpdatingSlotTime =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Boolean>("isUpdatingSlotTime")
                        ?: false
                Log.d("SlotAdderScr", "AppNavigation: $isUpdatingSlotTime")
                SlotAdderScreen( navController = navController, isUpdating = isUpdatingSlotTime)
            }
        composable(Screens.ChatScreen.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            val image =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("image").toString()
            val name =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("name").toString()
            val uid =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("uid").toString()
            val phoneNumber =
                navController.previousBackStackEntry?.savedStateHandle?.get<String>("phoneNumber").toString()
            ChatScreen(image, name,uid,navController,phoneNumber)
        }
        composable(Screens.UpdateProfile.route, enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }) {
            UpdateBarberInfoScreen(navController = navController)
        }
        }
}

