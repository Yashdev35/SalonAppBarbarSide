package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.components.DoubleCard
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar

@Composable
fun ProfileScreen(viewModel: GetBarberDataViewModel = hiltViewModel()){
    DoubleCard(midCarBody = { PhotoWithName() }, mainScreen = { ProfileScreenList() }, topAppBar = { BackButtonTopAppBar(
        onBackClick = { /*TODO*/ },
        title = "Profile"
    )})
}
@Composable
fun ProfileScreenList(){
    val profileList = listOf(
        Pair( R.drawable.salon_app_logo,"My Profile"),
        Pair( R.drawable.salon_app_logo,"My Booking History"),
        Pair( R.drawable.salon_app_logo,"Favorite's Saloon"),
        Pair( R.drawable.salon_app_logo,"Privacy Policy"),
        Pair( R.drawable.salon_app_logo,"About Us"),
        Pair( R.drawable.salon_app_logo,"Log Out"),
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(22.dp)
        .verticalScroll(rememberScrollState())) {

        ShowingList(image = R.drawable.salon_app_logo, text = "My Profile"){}
        ShowingList(image = R.drawable.salon_app_logo, text ="My Booking History"){}
        ShowingList(image = R.drawable.salon_app_logo, text = "Favorite's Saloon"){}
        ShowingList(image = R.drawable.salon_app_logo, text = "Privacy Policy"){}
        ShowingList(image = R.drawable.salon_app_logo, text ="About Us"){}
        ShowingList(image = R.drawable.salon_app_logo, text ="Log Out"){}


    }
}

@Composable
fun PhotoWithName(viewModel: GetBarberDataViewModel= hiltViewModel()){
    Column(modifier = Modifier
        , horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter =  rememberAsyncImagePainter(
            model = "https://firebasestorage.googleapis.com/v0/b/sallon-app-6139e.appspot.com/o/profile_image%2FptNTxkdC31NBaSQbJT5cnQQHO2u2.jpg?alt=media&token=ed411c18-99ad-4db2-94ab-23e1c9b2b1b6"
        //viewModel._barber.value.imageUri
        ), contentDescription ="userImage", modifier = Modifier
            .size(100.dp)
            .clip(CircleShape) )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = viewModel._barber.value.name.toString(),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
fun ShowingList(image:Int,text:String,onClick:()->Unit){
    Row(verticalAlignment = Alignment.CenterVertically){
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