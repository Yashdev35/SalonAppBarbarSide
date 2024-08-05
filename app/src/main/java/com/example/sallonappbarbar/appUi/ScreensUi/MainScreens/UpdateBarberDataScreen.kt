package com.example.sallonappbarbar.appUi.ScreensUi.MainScreens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.components.ShimmerEffectBarber
import com.example.sallonappbarbar.appUi.viewModel.AllBarberInfoViewModel
import com.example.sallonappbarbar.appUi.viewModel.BarberEvent
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.practicecoding.sallonapp.appui.components.GeneralButton

@Composable
fun UpdateBarberInfoScreen(
    allBarberInfoViewModel: AllBarberInfoViewModel = hiltViewModel(),
    viewModel: GetBarberDataViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) } // Initially show loading
    val barber = allBarberInfoViewModel.barber.value
    val scrollState = rememberScrollState()
    // Create mutable states with default values
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val shopName = remember { mutableStateOf(TextFieldValue("")) }
    val phoneNumber = remember { mutableStateOf(TextFieldValue("")) }
    val shopStreetAddress = remember { mutableStateOf(TextFieldValue("")) }
    val city = remember { mutableStateOf(TextFieldValue("")) }
    val state = remember { mutableStateOf(TextFieldValue("")) }
    val aboutUs = remember { mutableStateOf(TextFieldValue("")) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // When the barber data is loaded, update the states
    LaunchedEffect(barber) {
        if (barber != null) {
            name.value = TextFieldValue(barber.name ?: "")
            shopName.value = TextFieldValue(barber.shopName ?: "")
            phoneNumber.value = TextFieldValue(barber.phoneNumber ?: "")
            shopStreetAddress.value = TextFieldValue(barber.shopStreetAddress ?: "")
            city.value = TextFieldValue(barber.city ?: "")
            state.value = TextFieldValue(barber.state ?: "")
            aboutUs.value = TextFieldValue(barber.aboutUs ?: "")
            imageUri = barber.imageUri?.toUri()
            isLoading = false // Data is loaded, stop showing loading indicator
        }
    }

    if (isLoading) {
        ShimmerEffectBarber()
    } else {
        val imagePickerLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                imageUri = uri!!
                selectedImageUri = imageUri
            }

        Log.d("UpdateBarberInfoScreen", "UpdateBarberInfoScreen: $barber")
        Log.d("UpdateBarberInfoScreen", "UpdateBarberInfoScreen: ${name.value.text}, ${shopName.value.text}, ${phoneNumber.value.text}, ${shopStreetAddress.value.text}, ${city.value.text}, ${state.value.text}, ${aboutUs.value.text}, ${imageUri.toString()}")

        // UI components follow here...
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
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
                value = shopStreetAddress.value,
                onValueChange = { shopStreetAddress.value = it },
                label = { Text("Shop Street Address") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = purple_200,
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "Shop Street Address",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (shopStreetAddress.value.text.isNotEmpty()) {
                        IconButton(onClick = { shopStreetAddress.value = TextFieldValue("") }) {
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
                value = city.value,
                onValueChange = { city.value = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = purple_200,
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "City",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (city.value.text.isNotEmpty()) {
                        IconButton(onClick = { city.value = TextFieldValue("") }) {
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
                value = state.value,
                onValueChange = { state.value = it },
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Purple80,
                    unfocusedBorderColor = purple_200,
                    errorBorderColor = purple_200
                ),
                leadingIcon = {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "State",
                        Modifier.size(16.dp),
                        tint = Color.Black
                    )
                },
                trailingIcon = {
                    if (state.value.text.isNotEmpty()) {
                        IconButton(onClick = { state.value = TextFieldValue("") }) {
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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            GeneralButton(text = "Update your Info", width = 350, height = 80, modifier = Modifier) {
                val updatedBarber = BarberModel(
                    name = if (name.value.text.isNotEmpty() && name.value.text != viewModel.barber.value.name) name.value.text else viewModel.barber.value.name,
                    shopName = if (shopName.value.text.isNotEmpty() && shopName.value.text != viewModel.barber.value.shopName) shopName.value.text else viewModel.barber.value.shopName,
                    phoneNumber = if (phoneNumber.value.text.isNotEmpty() && phoneNumber.value.text != viewModel.barber.value.phoneNumber) phoneNumber.value.text else viewModel.barber.value.phoneNumber,
                    shopStreetAddress = if (shopStreetAddress.value.text.isNotEmpty() && shopStreetAddress.value.text != viewModel.barber.value.shopStreetAddress) shopStreetAddress.value.text else viewModel.barber.value.shopStreetAddress,
                    city = if (city.value.text.isNotEmpty() && city.value.text != viewModel.barber.value.city) city.value.text.trim().replace(" ", "").replaceFirstChar { it.uppercase() } else viewModel.barber.value.city,
                    state = if (state.value.text.isNotEmpty() && state.value.text != viewModel.barber.value.state) state.value.text.trim().replace(" ", "").replaceFirstChar { it.uppercase() } else viewModel.barber.value.state,
                    aboutUs = if (aboutUs.value.text.isNotEmpty() && aboutUs.value.text != viewModel.barber.value.aboutUs) aboutUs.value.text else viewModel.barber.value.aboutUs,
                    lat = viewModel.barber.value.lat,
                    long = viewModel.barber.value.long,
                )
                isLoading = true
                if (selectedImageUri == null) {
                    selectedImageUri = imageUri
                }
                viewModel.onEvent(BarberEvent.updateBarber(updatedBarber, selectedImageUri!!, context,navController))
                Log.d("UpdateBarberInfoScreen", "UpdateBarberInfoScreen: $updatedBarber")
            }
        }
    }

    }

@Preview(showBackground = true)
@Composable
fun UpdateBarberInfoScreenPreview() {
//    UpdateBarberInfoScreen()
}

