package com.example.sallonappbarbar.appUi.Screens.initiators

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSignUpScreen(
    navController: NavController,
    phoneNumber: String? = null,
    activity: Activity,
    viewModel: BarberDataViewModel = hiltViewModel(),
) {
    val auth = FirebaseAuth.getInstance()
    val phone = phoneNumber ?: "1234567890"
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedSalonType by remember { mutableStateOf<SalonType?>(null) }
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
    if (isDialog)
        LoadingAnimation()

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
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    value = phone,
                    enabled = false,
                    onValueChange = { },
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




                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onClick = {
                        dropdownExpanded = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = Color.White
                    ),
                    border = BorderStroke(0.5.dp, colorResource(id = R.color.grey_light))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.scissors),
                            contentDescription = "drop down",
                            modifier = Modifier
                                .size(20.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Text(
                            text = selectedSalonType?.label ?: "Male",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                        androidx.compose.material3.DropdownMenu(
                            expanded = dropdownExpanded, onDismissRequest = {
                                dropdownExpanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Male", style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    selectedSalonType = SalonType.MALE
                                    dropdownExpanded = false
                                })
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Female",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    selectedSalonType = SalonType.FEMALE
                                    dropdownExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Hybrid",
                                        style = MaterialTheme.typography.bodySmall,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    selectedSalonType = SalonType.HYBRID
                                    dropdownExpanded = false
                                }
                            )
                        }
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
                    value = streetAddress,
                    onValueChange = { streetAddress = it },
                    label = { Text("Enter local street address") },
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
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
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
                    value = state,
                    onValueChange = { state = it },
                    label = { Text("State") },
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
                    if (name.isNotBlank() && selectedSalonType != null && city.isNotBlank() && state.isNotBlank() && streetAddress.isNotBlank() &&
                        shopName.isNotBlank() && aboutUs.isNotBlank()
                    ) {
                        val barberModel = BarberModel(
                            name = name,
                            shopName = shopName,
                            phoneNumber = phoneNumber.toString(),
                            saloonType = selectedSalonType?.label,
                            imageUri = selectedImageUri.toString(),
                            shopStreetAddress = streetAddress,
                            city = city,
                            state = state,
                            aboutUs = aboutUs,
                            noOfReviews = 0,
                            rating = 0.0,
                            lat = 0.0,
                            long = 0.0,
                            uid = auth.currentUser?.uid.toString()
                        )
                        scope.launch(Dispatchers.Main) {
                            viewModel.addUserData(barberModel, selectedImageUri, activity).collect {
                                when (it) {
                                    is Resource.Success -> {
                                        isDialog = false
                                        activity.showMsg(it.result)
                                        navController.navigate(Screenes.SelecterScr.route)

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
                    } else {
                        Toast.makeText(
                            context,
                            "Either a field is empty or an image is not selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}


enum class SalonType(val label: String) {
    MALE("Male salon"),
    FEMALE("Female salon"),
    HYBRID("Hybrid salon"),
}

@Preview(showBackground = true)
@Composable
fun AdvancedSignUpScreenPreview() {
    val navController = rememberNavController()
    val activity = Activity()
    AdvancedSignUpScreen(
        navController = navController,
        phoneNumber = "1234567890",
        activity = activity
    )
}