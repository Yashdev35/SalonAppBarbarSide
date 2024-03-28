package com.example.sallonappbarbar.appUi

sealed class Screenes(
    val route : String
) {

    object Logo : Screenes("logo")
    object OnBoardingScreenes : Screenes("onBoardingScreens")
    object PhoneNumberScreen : Screenes("phoneNumberScreen")
    object OTPVerification : Screenes("otpVerification")
    object BarbarsSignUp : Screenes("barbarsSignUp")
    object SelecterScr : Screenes("selectServices")

    object PriceSelector : Screenes("priceSelector")
}