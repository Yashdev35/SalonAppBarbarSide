package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.viewModel.AllBarberInfoViewModel
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.ServiceViewModel
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: GetBarberDataViewModel = hiltViewModel(),
    allBarberInfoViewModel: AllBarberInfoViewModel = hiltViewModel()
){
    DoubleCard(
        midCarBody = { PhotoWithName(viewModel) },
        mainScreen = { ProfileScreenList(
            navController = navController
        ) },
        topAppBar = { BackButtonTopAppBar(
        onBackClick = { navController.navigate(Screens.Home.route){
            navController.popBackStack()
        } },
        title = "Profile"
    )})
}
@Composable
fun ProfileScreenList(
    navController: NavController,
){
    val profileList = listOf(
        Pair( R.drawable.salon_app_logo,"My Profile"),
        Pair( R.drawable.salon_app_logo,"Edit Shop"),
        Pair( R.drawable.salon_app_logo,"Favorite's Saloon"),
        Pair( R.drawable.salon_app_logo,"Privacy Policy"),
        Pair( R.drawable.salon_app_logo,"About Us"),
        Pair( R.drawable.salon_app_logo,"Log Out"),
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(22.dp)
        .verticalScroll(rememberScrollState())) {

        ShowingList(image = R.drawable.img, text = "Update Profile"){
            navController.navigate(Screens.UpdateProfile.route)
        }
        ShowingList(image = R.drawable.salon_app_logo, text ="Edit Services"){
            val isUpdatingService = true
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "isUpdatingService",
                value = isUpdatingService
            )
            navController.navigate(Screens.SelecterScr.route)
        }
        ShowingList(image = R.drawable.salon_app_logo, text = "Edit Service Prices"){
            val isUpdatingService = true
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "isUpdatingService",
                value = isUpdatingService
            )
            navController.navigate(Screens.PriceSelector.route)
        }
        ShowingList(image = R.drawable.salon_app_logo, text = "Edit Slot Timings"){
            val isUpdatingSlotTime = true
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "isUpdatingSlotTime",
                value = isUpdatingSlotTime
            )
            navController.navigate(Screens.SlotAdderScr.route)
        }
        ShowingList(image = R.drawable.salon_app_logo, text ="About Us"){}
        ShowingList(image = R.drawable.salon_app_logo, text ="Log Out"){}
    }
}

@Composable
fun PhotoWithName(viewModel: GetBarberDataViewModel= hiltViewModel()){
    Column(modifier = Modifier
        , horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter =  rememberAsyncImagePainter(
            model = viewModel.barber.value.imageUri
        ), contentDescription ="userImage", modifier = Modifier
            .size(100.dp)
            .clip(CircleShape) )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = viewModel.barber.value.name.toString(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun ShowingList(image:Int,text:String,onClick:()->Unit){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        ){
        Image(painter = painterResource(id =  image), contentDescription = text, modifier = Modifier
            .size(40.dp)
            .clip(CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text=text, color = Color.Black)
    }
    Spacer(modifier = Modifier.height(6.dp))
    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    Spacer(modifier = Modifier.height(6.dp))
}