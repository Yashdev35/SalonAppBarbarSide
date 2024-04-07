package com.example.sallonappbarbar.appUi.Screens.initiators


import android.app.Activity
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.appUi.utils.showMsg
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.example.sallonappbarbar.data.model.BarberModel
import com.example.sallonappbarbar.data.model.aService
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.CommonDialog
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
                    text = service.serviceTypeHeading,
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
                    text = aService.serviceTypeHeading,
                )
                Text(
                    text = aService.price,
                )
            }
        }
    }

@Composable
fun ServiceCard(
    aService: aService,
    ) {
//    var serviceLevel by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var showPriceInputDialog by remember { mutableStateOf(false) }
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
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
                        text = aService.serviceTypeHeading,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(

                    ){
                        Text(
                            text = aService.price,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                showPriceInputDialog = true
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Variants")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card {
            ServiceStandardAndPriceList(aService)
        }
    }
    if(showPriceInputDialog){
        AlertDialog(
            onDismissRequest = { showPriceInputDialog = false },
            confirmButton = {
                            Button(onClick = {
                                if(servicePrice.isNotEmpty()) {
                                    aService.price = servicePrice
                                    showPriceInputDialog = false
                                    servicePrice = ""
                                }
                            }) {
                                Text("Add")
                            }
                            },
            dismissButton = {
                            Button(
                                onClick = {
                                    showPriceInputDialog = false
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
                            .padding(bottom = 8.dp)
                    )
                }
            }
            )
    }

}

@Composable
fun ServiceSelectorScreen(
    barberData: BarberModel,
    navController: NavController,
) {
    var selectedServices by remember { mutableStateOf(emptyList<aService>()) }
    val serviceTypes = listOf(
        ServiceType(
            serviceTypeHeading = "Hair Services",
            aServices = listOf(
                aService(false, price = "0", id = 1, serviceTypeHeading = "Hair Service", serviceName = "Hair Cut"),
                aService(
                    false,
                    price = "$50",
                    id = 2,
                    serviceTypeHeading = "Hair Service",
                    serviceName = "Hair color"
                ),
                aService( price = "0", id = 3, serviceTypeHeading = "Hair Service", serviceName = "Hair style"),
            )
        ),
        ServiceType(
            serviceTypeHeading = "Nail Services",
            aServices = listOf()
        )
    )
    Surface(color = purple_200) {


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
@Composable
fun PriceSelector(
    viewModel: BarberDataViewModel = hiltViewModel(),
    navController: NavController,
    aServices: List<aService>,
    activity: Activity
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    if(isDialogVisible){
        LoadingAnimation()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Selected Services",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            aServices!!.forEach { service ->
                ServiceCard(aService = service)
                Spacer(modifier = Modifier.height(16.dp))
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
                    viewModel.addServiceData(aServices,activity).collect {
                        when (it) {
                            is Resource.Success -> {
                                isDialogVisible = false
                                activity.showMsg(it.result)
//                                navController.navigate(Screenes.Home.route)
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
