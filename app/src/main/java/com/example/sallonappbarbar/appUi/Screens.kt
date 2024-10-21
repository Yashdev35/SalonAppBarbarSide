package com.example.sallonappbarbar.appUi

sealed class Screens(
    val route : String
) {

    object Logo : Screens("logo")
    object OnBoardingScreenes : Screens("onBoardingScreens")
    object PhoneNumberScreen : Screens("phoneNumberScreen")
    object OTPVerification : Screens("otpVerification")
    object BarbarsSignUp : Screens("barbarsSignUp")
    object SelecterScr : Screens("selectServices")

    object PriceSelector : Screens("priceSelector")
    object Home : Screens("homeScreen")
    object SlotAdderScr : Screens("slotAdderScreen")
    object MainScreen:Screens("main_screen")
    object ChatScreen:Screens("chat_screen")
    object UpdateProfile:Screens("update_profile")
    object BookingHistory:Screens("booking_history_screen")


}