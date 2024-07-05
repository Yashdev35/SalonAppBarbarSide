package com.example.sallonappbarbar.appUi.ScreensUi.initiators


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.appUi.Screens
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.ServiceCat
import com.example.sallonappbarbar.data.model.ServiceModel
import com.example.sallonappbarbar.data.model.aService
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.CommonDialog
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@Composable
fun ServiceTypeItem(
    serviceType: ServiceCat,
    onServiceSelectedChange: (ServiceModel, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = serviceType.type,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        serviceType.services.forEach { service ->
            var isServiceSelected by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .padding(start = 6.dp, end = 6.dp)
                    .clip(MaterialTheme.shapes.medium),
                verticalAlignment = CenterVertically
            ) {
                Checkbox(
                    checked = isServiceSelected,
                    onCheckedChange = { isChecked ->
                        isServiceSelected = isChecked
                        service.isServiceSelected = isChecked
                        onServiceSelectedChange(service, isChecked)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = sallonColor,
                        uncheckedColor = Color.Black
                    )
                )
                Text(
                    text = service.serviceName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ServiceStandardAndPriceList(service : ServiceModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Rs."+service.price,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text = service.time+" mins",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
        }
    }

@Composable
fun ServiceCard(
    service: ServiceModel,
    ) {
    val showPriceAndTimeInputDialog = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                contentColor = Color.White,
                containerColor = Color.White,
            ), border = BorderStroke(2.dp, sallonColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = service.serviceName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                        IconButton(
                            onClick = {
                                showPriceAndTimeInputDialog.value = true
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "edit",tint= sallonColor)
                        }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Card(
            colors = CardDefaults.cardColors(
                contentColor = Color.White,
                containerColor = Color.White,
            ),border = BorderStroke(2.dp, sallonColor)
        ) {
            ServiceStandardAndPriceList(service)
        }
    }
    if(showPriceAndTimeInputDialog.value){
        TimeAndPriceEditorDialog(
            service = service,
            showPriceAndTimeInputDialog = showPriceAndTimeInputDialog
        )
    }
}

@Composable
fun TimeAndPriceEditorDialog(
    service: ServiceModel,
    showPriceAndTimeInputDialog: MutableState<Boolean>,
){
    var servicePrice by remember { mutableStateOf("") }
    var serviceTime by remember { mutableStateOf("") }
    AlertDialog(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).border(2.dp, sallonColor,RoundedCornerShape(20.dp)).background(Color.White),
        onDismissRequest = { showPriceAndTimeInputDialog.value = false },
        confirmButton = {
            Button(onClick = {
                if(servicePrice.isNotEmpty()) {
                    service.price = servicePrice
                    service.time = "$serviceTime "
                    servicePrice = ""
                    serviceTime = ""
                    showPriceAndTimeInputDialog.value = false
                }
            },colors = ButtonDefaults.buttonColors(containerColor = sallonColor)) {
                Text("Add",color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showPriceAndTimeInputDialog.value = false
                },colors = ButtonDefaults.buttonColors(containerColor = sallonColor)
            ) {
                Text("Cancel",color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp)
            }
        },
        containerColor = Color.White,
        title = { Text("Add Service Price and Time", color = Color.Black)},
        text = {
            Column {
                OutlinedTextField(
                    value = servicePrice,
                    onValueChange = { servicePrice = it },
                    label = { Text("Service Price") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = sallonColor,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = sallonColor,
                        focusedTextColor = Color.Black
                        , unfocusedLabelColor = Color.Gray,
                        cursorColor = sallonColor,
                        unfocusedTextColor = Color.Gray
                    )
                )
                OutlinedTextField(
                    value = serviceTime,
                    onValueChange = { serviceTime = it },
                    label = { Text("Service Time") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = sallonColor,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = sallonColor,
                        focusedTextColor = Color.Black
                        , unfocusedLabelColor = Color.Gray,
                        cursorColor = sallonColor,
                        unfocusedTextColor = Color.Gray
                    )
                )
            }
        }
    )
}

@Composable
fun ServiceSelectorScreen(
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    var selectedServices by remember { mutableStateOf(emptyList<ServiceModel>()) }
    val serviceTypes = listOf(
        ServiceCat(
            type = "Hair Services",
            services = listOf(
                ServiceModel(isServiceSelected = false,  id = 1, type = "Hair Service", serviceName = "Hair Cut"),
                ServiceModel(isServiceSelected = false,  id = 2, type = "Hair Service", serviceName = "Hair color",),
                ServiceModel(isServiceSelected = false , id = 3, type = "Hair Service", serviceName = "Hair style",),
            )
        ),
        ServiceCat(
            type = "Nail Services",
            services = listOf(
                ServiceModel(isServiceSelected =false, id = 4, type = "Nail Service", serviceName = "Manicure"),
                ServiceModel(isServiceSelected =false,  id = 5, type = "Nail Service", serviceName = "Pedicure",),
                ServiceModel(isServiceSelected =false,  id = 6, type = "Nail Service", serviceName = "Nail Art"),
            )
        ),
        ServiceCat(
            type = "Facial Services",
            services = listOf(
                ServiceModel(isServiceSelected =false, id = 7, type = "Facial Service", serviceName = "Clean Up"),
                ServiceModel(isServiceSelected =false,  id = 8, type = "Facial Service", serviceName = "Facial"),
                ServiceModel(isServiceSelected =false, id = 9, type = "Facial Service", serviceName = "Bleach"),
            )

        )
    )
    Surface(color = purple_200) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BackButtonTopAppBar(onBackClick = { /*TODO*/ }, title = "Service Available")
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color.White)
            ) {
                item(){Spacer(modifier = Modifier.height(20.dp))}
                items(serviceTypes) { serviceType ->
                    ServiceTypeItem(serviceType = serviceType) { service, isSelected ->
                        selectedServices = if (isSelected) {
                            /*List of services selected by the are stored here*/
                            selectedServices + service
                        } else {
                            selectedServices.filter { it.id != service.id }
                        }
                    }
                }
            }
            GeneralButton(text = "Next", width = 350, height = 80, modifier = Modifier) {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "services",
                    value = selectedServices
                )
                navController.navigate(Screens.PriceSelector.route)
            }
        }
    }
}
fun aServiceSorter(services : List<ServiceModel>) : List<ServiceCat> {
    val serviceTypes = mutableListOf<ServiceCat>()
    services.forEach { service ->
        val serviceType = serviceTypes.find { it.type == service.type }
        if (serviceType != null) {
            serviceType.services += service
        } else {
            serviceTypes.add(ServiceCat(service.type, listOf(service)))
        }
    }
    return serviceTypes
}

@Composable
fun PriceSelector(
//    barberData: BarberModel,
    viewModel: BarberDataViewModel = hiltViewModel(),
    navController: NavController,
    services: List<ServiceModel>,
    activity: Activity
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (isDialogVisible) {
        CommonDialog()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(purple_200), // Set the background color to purple
        contentAlignment = Alignment.TopCenter
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                BackButtonTopAppBar(onBackClick = { /*TODO*/ }, title = "Price Selector")
            }
            items(services) { service ->
                ServiceCard(service = service)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.White
            )
        ) {
            GeneralButton(text = "Next", width = 350, height = 80, modifier = Modifier) {
                Log.d("Barber", "PriceSelector: $services")
                scope.launch(Dispatchers.Main) {
                    val serviceTypes = aServiceSorter(services)
                    viewModel.addServiceData(serviceTypes, activity).collect {
                        when (it) {
                            is Resource.Success -> {
                                isDialogVisible = false
                                activity.showMsg(it.result)
                                navController.navigate(Screens.SlotAdderScr.route)
                            }
                            is Resource.Failure -> {
                                isDialogVisible = false
                                activity.showMsg(it.exception.toString())
                            }
                            Resource.Loading -> {
                                isDialogVisible = true
                            }
                        }
                    }
                }
            }
        }
    }
}

