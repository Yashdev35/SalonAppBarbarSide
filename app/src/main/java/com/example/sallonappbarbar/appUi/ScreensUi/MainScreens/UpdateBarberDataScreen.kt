package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.components.AlertDialogBox
import com.example.sallonappbarbar.appUi.components.ShimmerEffectBarber
import com.example.sallonappbarbar.appUi.viewModel.AllBarberInfoViewModel
import com.example.sallonappbarbar.appUi.viewModel.BarberEvent
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.LocationViewModel
import com.example.sallonappbarbar.data.model.LocationModel
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import com.practicecoding.sallonapp.appui.components.Purple200Button
import java.util.Locale

@Composable
fun UpdateBarberInfoScreen(
    allBarberInfoViewModel: AllBarberInfoViewModel = hiltViewModel(),
    viewModel: GetBarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val location by locationViewModel.getLocationLiveData().observeAsState()
    var locationDetails by remember {
        mutableStateOf(LocationModel(null, null, null, null, null, null, null))
    }
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address>? = location?.latitude?.let {
        geocoder.getFromLocation(
            it.toDouble(), location!!.longitude!!.toDouble(), 1
        )
    }
    if (!addresses.isNullOrEmpty()) {
        val address = addresses[0]
        locationDetails = LocationModel(
            location!!.latitude,
            location!!.longitude,
            address.subLocality,
            address.subThoroughfare,
            address.locality,
            address.adminArea,
            address.countryName
        )
//        Toast.makeText(context,locationDetails.latitude,Toast.LENGTH_SHORT).show()
    }
    var isLoading by remember { mutableStateOf(true) } // Initially show loading
    var showDialog by remember {
        mutableStateOf(false)
    }
    val barber = allBarberInfoViewModel.barber.value
    val scrollState = rememberScrollState()
    // Create mutable states with default values
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val shopName = remember { mutableStateOf(TextFieldValue("")) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue("")) }
    val aboutUs = remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // When the barber data is loaded, update the states
    LaunchedEffect(barber) {
        if (barber != null) {
            name.value = TextFieldValue(barber.name ?: "")
            shopName.value = TextFieldValue(barber.shopName ?: "")
            phoneNumber.value = TextFieldValue(barber.phoneNumber ?: "")
            aboutUs.value = TextFieldValue(barber.aboutUs ?: "")
            imageUri = barber.imageUri?.toUri()
            isLoading = false
        }
    }

    if (showDialog) {
        AlertDialogBox(
            title = "Change Location",
            message = "Want to change the current location on salon",
            onConfirmButton = {
                Purple200Button(text = "Change", onClick = {
                    showDialog=false
                    barber.shopStreetAddress =
                        if (locationDetails.streetAddress == null) {
                            ""
                        } else {
                            locationDetails.streetNumber + locationDetails.streetAddress
                        }
                    barber.city = locationDetails.city
                    barber.state = locationDetails.state
                    isLoading = true

                    viewModel.onEvent(
                        BarberEvent.updateBarber(
                            barber,
                            selectedImageUri,
                            context,
                            navController
                        )
                    )
                })

            },
            onDismissRequest = {
                showDialog = false
            },
            onDismissButton = {
                Purple200Button(text = "No", onClick = {
                    showDialog=false
                    isLoading = true

                    viewModel.onEvent(
                        BarberEvent.updateBarber(
                            barber,
                            selectedImageUri,
                            context,
                            navController
                        )
                    )

                })

            }
        )
    }

    if (isLoading) {
        LoadingAnimation()
    } else {
        val imagePickerLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                imageUri = uri!!
                selectedImageUri = imageUri
            }

        Log.d("UpdateBarberInfoScreen", "UpdateBarberInfoScreen: $barber")
        Log.d(
            "UpdateBarberInfoScreen",
            "UpdateBarberInfoScreen: ${name.value.text}, ${shopName.value.text}, ${phoneNumber.value.text}, ${aboutUs.value.text}, ${imageUri.toString()}"
        )

        // UI components follow here...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White)
                .padding(vertical = 25.dp, horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Update your Info", color = Color.Black, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                // Background image
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Avatar Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1.0f)
                        .width(150.dp)
                        .height(150.dp)
                        .clip(shape = CircleShape)
                )
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Add Photo",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                        .align(Alignment.BottomEnd),
                    tint = Color.Black
                )
            }
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80, // Change the outline color when focused
                    unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Name",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (name.value.text.isNotEmpty()) {
                        IconButton(onClick = { name.value = TextFieldValue("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = shopName.value,
                onValueChange = { shopName.value = it },
                label = { Text("Shop Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = purple_200,
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.scissors),
                        contentDescription = "Shop Name",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (shopName.value.text.isNotEmpty()) {
                        IconButton(onClick = { shopName.value = TextFieldValue("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = aboutUs.value,
                onValueChange = { aboutUs.value = it },
                label = { Text("About Us") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = purple_200,
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "About Us",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (aboutUs.value.text.isNotEmpty()) {
                        IconButton(onClick = { aboutUs.value = TextFieldValue("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Black
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            GeneralButton(
                text = "Update your Info",
                width = 350,
                height = 80,
                modifier = Modifier
            ) {
                barber.name =
                    if (name.value.text.isNotEmpty() && name.value.text != viewModel.barber.value.name) name.value.text else viewModel.barber.value.name
                barber.shopName =
                    if (shopName.value.text.isNotEmpty() && shopName.value.text != viewModel.barber.value.shopName) shopName.value.text else viewModel.barber.value.shopName
                barber.phoneNumber =
                    if (phoneNumber.value.text.isNotEmpty() && phoneNumber.value.text != viewModel.barber.value.phoneNumber) phoneNumber.value.text else viewModel.barber.value.phoneNumber
                barber.aboutUs =
                    if (aboutUs.value.text.isNotEmpty() && aboutUs.value.text != viewModel.barber.value.aboutUs) aboutUs.value.text else viewModel.barber.value.aboutUs

                showDialog = true


            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun UpdateBarberInfoScreenPreview() {
//    UpdateBarberInfoScreen()
}

