package com.example.sallonappbarbar.appUi.Screens.initiators

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.unit.sp
import androidx.compose.material.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.model.BarberModelResponse
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSignUpScreen(
    phoneNumber: String? = null,
    viewModel: BarberDataViewModel = hiltViewModel()
) {
    val phone = phoneNumber ?: "1234567890"
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedSalonType by remember { mutableStateOf<SalonType?>(null) }
    val focusManager = LocalFocusManager.current
    var shopName by remember { mutableStateOf(" ") }
    var shopAddress by remember { mutableStateOf("") }
    var selectedImagesUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(top = 16.dp)
            .clickable( // Dismiss keyboard
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.salon_app_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                text = "Let's get you started!",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive)
                ,
                color = Color.Black
            )
            androidx.compose.material3.OutlinedTextField(
                value = phone,
                enabled = false,
                onValueChange = { },
                label = { androidx.compose.material3.Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(
                        sallonColor.toArgb()
                    ).copy(alpha = 0.6f),
                    unfocusedBorderColor = Color(
                        purple_200.toArgb()
                    ).copy(alpha = 0.3f),
                    focusedTextColor = Color.Black,
                    cursorColor = Color(
                        sallonColor.toArgb()
                    ),
                    focusedLabelColor = Color(
                        sallonColor.toArgb()
                    ),
                    unfocusedTextColor = Color.Black,

                    ),
                leadingIcon = {
                    androidx.compose.material3.Icon(
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
                    focusedBorderColor = Color.Red.copy(alpha = 0.6f),
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.3f),
                    textColor = Color.Black
                ),
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
                    backgroundColor = colorResource(id = R.color.white),
                    contentColor = colorResource(id = R.color.black),
                ),
                border = BorderStroke(0.5.dp, colorResource(id = R.color.black))
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
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = selectedSalonType?.label ?:"Male salon",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        textAlign = TextAlign.Start
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
                                    text = "Male salon", style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                selectedSalonType = SalonType.MALE
                                dropdownExpanded = false
                            })
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Female salon",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
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
                                    text = "Hybrid salon",
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
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
                value = shopName.toString(),
                onValueChange = { shopName = it },
                label = { Text("Name of your shop") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Red.copy(alpha = 0.6f),
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.6f),

                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(value = shopAddress,
                 onValueChange = {shopAddress = it},
                    label = { Text("Address of your shop") },
                    modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Red.copy(alpha = 0.6f),
                    unfocusedBorderColor = Color.Black.copy(alpha = 0.6f),

                    textColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )


            MultiplePhotoPicker(
                    selectedImagesUris = mutableStateOf(selectedImagesUris)
                    )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 45.dp, topEnd =45.dp),
            colors = CardColors(
                containerColor = colorResource(id = R.color.sallon_color),
                contentColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.sallon_color),
                disabledContentColor = colorResource(id = R.color.white),
            ),

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (name.isNotBlank() && selectedSalonType != null && shopName.isNotBlank()&&
                            selectedImagesUris.size > 4 && shopAddress.isNotBlank()
                        ) {
                            try {
                                scope.launch {
                                    viewModel.insertData(
                                        item = BarberModelResponse.BarberModelItem(
                                            name = name,
                                            shopName = shopName,
                                            phoneNumber = phone,
                                            saloonType = selectedSalonType?.label,
                                            imageUris = selectedImagesUris.map { it.toString() },
                                            shopAddress = shopAddress
                                        )
                                    )
                                }
                            }catch (e:Exception){
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Either a field is empty or did not select more than 4 images",
                                Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white))
                ) {
                    Text("Sign Up", color = Color(sallonColor.toArgb())) // Purple
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
    AdvancedSignUpScreen()
}