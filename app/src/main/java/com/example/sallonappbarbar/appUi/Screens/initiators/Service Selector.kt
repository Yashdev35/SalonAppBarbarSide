package com.example.sallonappbarbar.appUi.Screens.initiators


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.aService
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.ui.theme.Purple40
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.GeneralButton
import com.practicecoding.sallonapp.appui.components.LoadingAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@Composable
fun ServiceTypeItem(
    serviceType: ServiceType,
    onServiceSelectedChange: (aService, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp)
    ) {
        Text(
            text = serviceType.serviceTypeHeading,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        serviceType.aServices.forEach { service ->
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
fun ServiceStandardAndPriceList(aService : aService) {
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
                )
                Text(
                    text = aService.price,
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
            )
            Text(
                text = aService.time,
            )
        }
        }
    }

@Composable
fun ServiceCard(
    aService: aService,
    ) {
    val showPriceAndTimeInputDialog = remember { mutableStateOf(false) }
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            colors = CardColors(
                contentColor = Color.White,
                containerColor = sallonColor,
                disabledContainerColor = sallonColor,
                disabledContentColor = Color.White,
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = aService.serviceName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                        IconButton(
                            onClick = {
                                showPriceAndTimeInputDialog.value = true
                            }
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "edit")
                        }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Card(
            colors = CardColors(
                contentColor = Color.White,
                containerColor = sallonColor,
                disabledContainerColor = sallonColor,
                disabledContentColor = Color.White,
            )
        ) {
            ServiceStandardAndPriceList(aService)
        }
    }
    if(showPriceAndTimeInputDialog.value){
        TimeAndPriceEditorDialog(
            aService = aService,
            showPriceAndTimeInputDialog = showPriceAndTimeInputDialog
        )
    }
}

@Composable
fun TimeAndPriceEditorDialog(
    aService: aService,
    showPriceAndTimeInputDialog: MutableState<Boolean>,
){
    var servicePrice by remember { mutableStateOf("") }
    var serviceTime by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { showPriceAndTimeInputDialog.value = false },
        confirmButton = {
            Button(onClick = {
                if(servicePrice.isNotEmpty()) {
                    aService.price = "$servicePrice \u20B9"
                    aService.time = "$serviceTime min"
                    servicePrice = ""
                    serviceTime = ""
                    showPriceAndTimeInputDialog.value = false
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    showPriceAndTimeInputDialog.value = false
                }
            ) {
                Text("Cancel")
            }
        },
        title = { Text("Add Service Variations")},
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
                    )
                )
            }
        }
    )
}

@Composable
fun ServiceSelectorScreen(
    barberData: BarberModel,
    navController: NavController,
) {
    val scrollState = rememberScrollState()
    var selectedServices by remember { mutableStateOf(emptyList<aService>()) }
    val serviceTypes = listOf(
        ServiceType(
            serviceTypeHeading = "Hair Services",
            aServices = listOf(
                aService(false, price = "0 ₹", id = 1, serviceTypeHeading = "Hair Service", serviceName = "Hair Cut",time = "0 min"),
                aService(false, price = "0 ₹", id = 2, serviceTypeHeading = "Hair Service", serviceName = "Hair color", time = "0 min"),
                aService(false ,price = "0 ₹", id = 3, serviceTypeHeading = "Hair Service", serviceName = "Hair style",time = "0 min"),
            )
        ),
        ServiceType(
            serviceTypeHeading = "Nail Services",
            aServices = listOf(
                aService(false, price = "0 ₹", id = 4, serviceTypeHeading = "Nail Service", serviceName = "Manicure",time = "0 min"),
                aService(false, price = "0 ₹", id = 5, serviceTypeHeading = "Nail Service", serviceName = "Pedicure",time = "0 min"),
                aService(false, price = "0 ₹", id = 6, serviceTypeHeading = "Nail Service", serviceName = "Nail Art",time = "0 min"),
            )
        ),
        ServiceType(
            serviceTypeHeading = "Facial Services",
            aServices = listOf(
                aService(false, price = "0 ₹", id = 7, serviceTypeHeading = "Facial Service", serviceName = "Clean Up",time = "0 min"),
                aService(false, price = "0 ₹", id = 8, serviceTypeHeading = "Facial Service", serviceName = "Facial",time = "0 min"),
                aService(false, price = "0 ₹", id = 9, serviceTypeHeading = "Facial Service", serviceName = "Bleach",time = "0 min"),
            )

        )
    )
    Surface(color = Purple40) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp, end = 6.dp, top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BackButtonTopAppBar(onBackClick = { /*TODO*/ }, title = "Service Available")
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(serviceTypes) { serviceType ->
                    ServiceTypeItem(serviceType = serviceType) { service, isSelected ->
                        if (isSelected) {
                            /*List of services selected by the are stored here*/
                            selectedServices = selectedServices + service
                        } else {
                            selectedServices = selectedServices.filter { it.id != service.id }
                        }
                    }
                }
            }
            GeneralButton(text = "Next", width = 350, height = 80, modifier = Modifier) {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "services",
                    value = selectedServices
                )
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "barber",
                    value = barberData
                )
                navController.navigate(Screenes.PriceSelector.route)
            }
        }
    }
}
fun aServiceSorter(aServices : List<aService>) : List<ServiceType> {
    val serviceTypes = mutableListOf<ServiceType>()
    aServices.forEach { service ->
        val serviceType = serviceTypes.find { it.serviceTypeHeading == service.serviceTypeHeading }
        if (serviceType != null) {
            serviceType.aServices = serviceType.aServices + service
        } else {
            serviceTypes.add(ServiceType(service.serviceTypeHeading, listOf(service)))
        }
    }
    return serviceTypes
}

@Composable
fun PriceSelector(
    viewModel: BarberDataViewModel = hiltViewModel(),
    navController: NavController,
    aServices: List<aService>,
    activity: Activity
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (isDialogVisible) {
        LoadingAnimation()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBB86FC)), // Set the background color to purple
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Selected Services",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(aServices) { service ->
                ServiceCard(aService = service)
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp
            )
        ) {
            GeneralButton(text = "Next", width = 350, height = 80, modifier = Modifier) {
                Log.d("Barber", "PriceSelector: $aServices")
                scope.launch(Dispatchers.Main) {
                    val serviceTypes = aServiceSorter(aServices)
                    viewModel.addServiceData(serviceTypes, activity).collect {
                        when (it) {
                            is Resource.Success -> {
                                isDialogVisible = false
                                activity.showMsg(it.result)
                                navController.navigate(Screenes.SlotAdderScr.route)
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

@Preview
@Composable
fun ServiceSelectorScreenPreview() {
    var context = Activity()

    var navController = rememberNavController()
    PriceSelector(navController = navController, aServices =
    listOf(
        aService(false, price = "0", id = 1, serviceTypeHeading = "Hair Service", serviceName = "Hair Cut",time = "1 hour"),
        aService(
            false,
            price = "$50",
            id = 2,
            serviceTypeHeading = "Hair Service",
            serviceName = "Hair color",
            time = "1 hour"
        ),
        aService( price = "0", id = 3, serviceTypeHeading = "Hair Service", serviceName = "Hair style",time = "1 hour"),
    )
        , activity = context)
}
