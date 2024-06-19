package com.example.sallonappbarbar.appUi.navigation

import android.app.Activity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.MainScreen1
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.SlotAdderScreen
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.TimeSelection
import com.example.sallonappbarbar.appUi.ScreensUi.MainScreens.daySelection
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.components.HeadingText
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.AdvancedSignUpScreen
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.PriceSelector
import com.example.sallonappbarbar.appUi.ScreensUi.initiators.ServiceSelectorScreen
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.Service
import com.example.sallonappbarbar.data.model.aService
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.screens.initiatorScreens.LogoScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.OtpVerificationScreen
import com.practicecoding.sallonapp.screens.initiatorScreens.PhoneNumberScreen
import java.time.LocalDate

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
    NavHost(navController = navController, startDestination = Screens.Logo.route) {
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
        composable(Screens.BarbarsSignUp.route + "/{phoneNumber}") { backStackEntry ->
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
        composable(Screens.SelecterScr.route) {
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
        composable(Screens.PriceSelector.route){
            val services = navController.previousBackStackEntry?.savedStateHandle?.get<List<aService>>("services")?: emptyList()
            val barberData = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")?:BarberModel(
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
            PriceSelector(
                barberData = barberData,
                navController = navController,
                aServices = services,
                activity = context as Activity
            )
        }
        composable(Screens.Home.route,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { popEnterTransition },
            popExitTransition = { popExitTransition }
            ){
            val barberData = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")?:BarberModel(
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
            MainScreen1(
                navHostController = navController,
                context = context)
        }
        composable(Screens.SlotAdderScr.route){
            val barberData = navController.previousBackStackEntry?.savedStateHandle?.get<BarberModel>("barber")?:BarberModel(
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
            SlotAdderScreen(context as Activity, navController = navController)
        }
    }
}
