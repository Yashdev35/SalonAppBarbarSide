package com.example.sallonappbarbar.appUi.Screens.initiators


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screenes
import com.example.sallonappbarbar.data.model.Service
import com.example.sallonappbarbar.data.model.ServiceType
import com.example.sallonappbarbar.ui.theme.Purple80
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.appui.components.BackButtonTopAppBar
import com.practicecoding.sallonapp.appui.components.GeneralButton

//data class Service(
//    val serviceName: String,
//    var isServiceSelected: Boolean = false,
//    val price: String,
//    val id: Int,
//)

//data class ServiceType(
//    val serviceTypeHeading: String,
//    val services: List<Service>
//)

@Composable
fun ServiceSelectorScreen(
    navController: NavController
) {
    var selectedServices by remember { mutableStateOf(emptyList<Service>()) }

    val serviceTypes = listOf(
        ServiceType(
            serviceTypeHeading = "Hair Services",
            services = listOf(
                Service("Haircut", price = "$20", id = 1, serviceTypeHeading = "Hair Services"),
                Service(
                    "Hair Coloring",
                    price = "$50",
                    id = 2,
                    serviceTypeHeading = "Hair Services"
                ),
                Service("Hair Styling", price = "$30", id = 3, serviceTypeHeading = "Hair Services")
            )
        ),
        ServiceType(
            serviceTypeHeading = "Nail Services",
            services = listOf(
                Service("Manicure", price = "$25", id = 4, serviceTypeHeading = "Nail Services"),
                Service("Pedicure", price = "$30", id = 5, serviceTypeHeading = "Nail Services"),
                Service("Nail Art", price = "$15", id = 6, serviceTypeHeading = "Nail Services")
            )
        ),
        ServiceType(
            serviceTypeHeading = "Facial Services",
            services = listOf(
                Service("Clean Up", price = "$20", id = 7, serviceTypeHeading = "Facial Services"),
                Service("Facial", price = "$30", id = 8, serviceTypeHeading = "Facial Services"),
                Service("Bleach", price = "$15", id = 9, serviceTypeHeading = "Facial Services")
            )
        ),
        ServiceType(
            serviceTypeHeading = "Massage Services",
            services = listOf(
                Service(
                    "Head Massage",
                    price = "$20",
                    id = 10,
                    serviceTypeHeading = "Massage Services"
                ),
                Service(
                    "Full Body Massage",
                    price = "$50",
                    id = 11,
                    serviceTypeHeading = "Massage Services"
                ),
                Service(
                    "Foot Massage",
                    price = "$15",
                    id = 12,
                    serviceTypeHeading = "Massage Services"
                )
            )
        ),
        ServiceType(
            serviceTypeHeading = "Spa Services",
            services = listOf(
                Service("Body Spa", price = "$50", id = 13, serviceTypeHeading = "Spa Services"),
                Service("Hair Spa", price = "$30", id = 14, serviceTypeHeading = "Spa Services"),
                Service("Foot Spa", price = "$15", id = 15, serviceTypeHeading = "Spa Services")
            )
        ),
        ServiceType(
            serviceTypeHeading = "Waxing Services",
            services = listOf(
                Service(
                    "Full Body Waxing",
                    price = "$50",
                    id = 16,
                    serviceTypeHeading = "Waxing Services"
                ),
                Service(
                    "Half Body Waxing",
                    price = "$30",
                    id = 17,
                    serviceTypeHeading = "Waxing Services"
                ),
                Service(
                    "Underarms Waxing",
                    price = "$15",
                    id = 18,
                    serviceTypeHeading = "Waxing Services"
                )
            )
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
                    key = "service",
                    value = selectedServices

                )
                navController.navigate(Screenes.PriceSelector.route)
            }
        }
    }
}

@Composable
fun ServiceTypeItem(
    serviceType: ServiceType,
    onServiceSelectedChange: (Service, Boolean) -> Unit
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
fun PriceSelector(
    navController: NavController,
    service: List<Service>?
) {
    var list by remember {
        mutableStateOf(mutableListOf<Int>())
    }
    var i = 0

    if (service != null) {
        for (barber in service) {
            Surface {
                Column {
                    Row {
                        Text(text = barber.serviceName, modifier = Modifier.padding(16.dp))
//                        OutlinedTextField(
//                            value = list[i++],
//                            onValueChange = { list[i]= it },
//                            placeholder = { "service" },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                focusedBorderColor = Purple80, // Change the outline color when focused
//                                unfocusedBorderColor = purple_200, // Change the outline color when unfocused
//                                errorBorderColor = purple_200
//                            ),
//
//
//
//                        )


                    }
                }
            }
        }


    }
}

//@Preview(showBackground = true)
//@Composable
//fun ServicesScreenPreview() {
//    val navController = rememberNavController()
//    ServiceSelectorScreen(navController)
//}