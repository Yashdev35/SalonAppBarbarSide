package com.example.sallonappbarbar.appUi.ScreensUi.initiators

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.components.CommonDialog
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.appUi.viewModel.LocationViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.LocationModel
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.components.GeneralButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


@Composable
fun AdvancedSignUpScreen(
    phoneNumber:String,
    navController: NavController,
    activity: Activity,
    viewModel: BarberDataViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    locationViewModel.startLocationUpdates()
    val location by locationViewModel.getLocationLiveData().observeAsState()
    var locationDetails by remember {
        mutableStateOf(LocationModel(null, null, null, null, null,null,null))
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
    val auth = FirebaseAuth.getInstance()
    var name by remember { mutableStateOf("") }
    var barberPhoneNumber by remember {
        mutableStateOf(phoneNumber)
    }
    var mExpanded by remember { mutableStateOf(false) }
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    var selectedSalonType by remember { mutableStateOf(" ") }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var shopName by remember { mutableStateOf(" ") }
    var streetAddress by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var aboutUs by remember { mutableStateOf("") }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var isDialog by remember { mutableStateOf(false) }

    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.parse("android.resource://${context.packageName}/${R.drawable.salon_app_logo}"))
    }
    val scope = rememberCoroutineScope()
    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            selectedImageUri = imageUri
        }

    val mCities = listOf("Male Salon", "Female Salon", "Unisex Salon")

    if (isDialog)
        CommonDialog()

    if (!isDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
                .clickable( // Dismiss keyboard
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var textFieldPosition by remember { mutableStateOf(Offset.Zero) }
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
                    // Icon for adding photo
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Add Photo",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
//                            singlePhotoPickerLauncher.launch(
//                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                            )
                                imagePickerLauncher.launch("image/*")

                            }
                            .align(Alignment.BottomEnd),
                        tint = Color.Black
                    )
                }
                OutlinedTextField(
                    value = barberPhoneNumber,
                    enabled = true,
                    onValueChange = {barberPhoneNumber=it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple80, // Change the outline color when focused
                        unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                        errorBorderColor = purple_200
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icons8_phone_50),
                            contentDescription = "Name",
                            Modifier.size(16.dp),
                            tint = Color.Black

                        )
                    },
                    trailingIcon = {
                        if (barberPhoneNumber.isNotEmpty()) {
                            IconButton(onClick = { barberPhoneNumber = "" }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Black,

                                    )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
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
                        if (name.isNotEmpty()) {
                            IconButton(onClick = { name = "" }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Black,

                                    )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                OutlinedTextField(
                    value = mSelectedText,
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.SemiBold),
                    onValueChange = { mSelectedText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            mTextFieldSize = coordinates.size.toSize()
                            textFieldPosition = coordinates.positionInWindow()
                        }
                        .padding(horizontal = 1.dp),
                    label = {
                        Text(
                            "Select a gender",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    trailingIcon = {
                        Icon(
                            icon,
                            contentDescription = null,
                            Modifier.clickable { mExpanded = !mExpanded },
                            tint = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color(0xFF6200EE),
                        disabledTrailingIconColor = Color.White
                    ),
                    enabled = false
                )

                DropdownMenu(
                    expanded = mExpanded,
                    onDismissRequest = { mExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
                        .background(if (mExpanded) Color.White else Color.Black),
                    offset = DpOffset(
                        x = 0.dp,
                        y = with(LocalDensity.current) { textFieldPosition.y.toDp() + mTextFieldSize.height.toDp() }
                    )
                ) {
                    mCities.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(text = label, color = Color.Black) },
                            onClick = {
                                mSelectedText = label
                                mExpanded = false
                            }
                        )
                    }
                }

                // out line text field for birth date
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    label = { Text("Name of your shop") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple80, // Change the outline color when focused
                        unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                        errorBorderColor = purple_200
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    trailingIcon = {
                        if (name.isNotEmpty()) {
                            IconButton(onClick = { name = "" }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Black,
                                )
                            }
                        }
                    }
                )

                OutlinedTextField(
                    value = aboutUs,
                    onValueChange = { aboutUs = it },
                    label = { Text("About us") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Purple80, // Change the outline color when focused
                        unfocusedBorderColor = purple_200, // Change the outline color when unfocused
                        errorBorderColor = purple_200
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = {
                        if (name.isNotEmpty()) {
                            IconButton(onClick = { name = "" }) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Black,

                                    )
                            }
                        }
                    },
                    maxLines = 5
                )

                GeneralButton(text = "Sign In", width = 350, height = 80, modifier = Modifier) {
                    if (name.isNotBlank()   && shopName.isNotBlank() && aboutUs.isNotBlank() && locationDetails.latitude != null && locationDetails.longitude != null && selectedImageUri != null
                    ) {
                        val barberModel = BarberModel(
                            name = name.trim(),
                            shopName = shopName.trim(),
                            phoneNumber = barberPhoneNumber.toString(),
                            saloonType = mSelectedText,
                            imageUri = selectedImageUri.toString(),
                            shopStreetAddress = if (locationDetails.streetAddress==null){""}else {
                                locationDetails.streetNumber + locationDetails.streetAddress
                            },
                            city = locationDetails.city,
                            state = locationDetails.state,
                            aboutUs = aboutUs,
                            noOfReviews = "0",
                            open = false,
                            rating = 0.0,
                            lat = locationDetails.latitude!!.toDouble(),
                            long = locationDetails.longitude!!.toDouble(),
                            uid = auth.currentUser?.uid.toString()
                        )
                        scope.launch(Dispatchers.Main) {
                            viewModel.addUserData(barberModel, selectedImageUri, activity).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        isDialog = false
                                        activity.showMsg(it.result)
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "BarberModel",
                                            barberModel
                                        )
                                        navController.navigate(Screens.SelecterScr.route)
                                    }

                                    is Resource.Failure -> {
                                        isDialog = false
                                        activity.showMsg(it.exception.toString())
                                    }

                                    Resource.Loading -> {
                                        isDialog = true
                                    }
                                }
                            }
                        }
                    } else if (selectedImageUri == null) {
                        Toast.makeText(
                            context,
                            "Please select an image",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (locationDetails.latitude == null || locationDetails.longitude == null) {
                        Toast.makeText(
                            context,
                            "Turn on your location",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill all the fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun AdvancedSignUpScreenPreview() {
//    val navController = rememberNavController()
//    val activity = Activity()
//    AdvancedSignUpScreen(
//        navController = navController,
//        phoneNumber = "1234567890",
//        activity = activity
//    )
//}