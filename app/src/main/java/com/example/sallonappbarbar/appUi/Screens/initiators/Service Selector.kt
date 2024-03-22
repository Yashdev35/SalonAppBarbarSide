package com.example.sallonappbarbar.appUi.Screens.initiators


import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.appUi.Screenes

data class Service(
    val serviceName: String,
    var isServiceSelected: Boolean = false,
    val price: String,
    val id: Int,
)

data class ServiceType(
    val serviceTypeHeading: String,
    val services: List<Service>
)

@Composable
fun ServiceSelectorScreen(
    navController: NavController
) {
    var selectedServices by remember { mutableStateOf(emptyList<Service>()) }

    val serviceTypes = listOf(
        ServiceType(
            serviceTypeHeading = "Hair Services",
            services = listOf(
                Service("Haircut", price = "$20", id = 1),
                Service("Hair Coloring", price = "$50", id = 2),
                Service("Hair Styling", price = "$30", id = 3)
            )
        ),
        ServiceType(
            serviceTypeHeading = "Nail Services",
            services = listOf(
                Service("Manicure", price = "$25", id = 4),
                Service("Pedicure", price = "$30", id = 5),
                Service("Nail Art", price = "$15", id = 6)
            )
        ),
        ServiceType(
            serviceTypeHeading = "Facial Services",
            services = listOf(
                Service("Clean Up", price = "$20", id = 7),
                Service("Facial", price = "$30", id = 8),
                Service("Bleach", price = "$15", id = 9)
            )
        ),
        ServiceType(
            serviceTypeHeading = "Massage Services",
            services = listOf(
                Service("Head Massage", price = "$20", id = 10),
                Service("Full Body Massage", price = "$50", id = 11),
                Service("Foot Massage", price = "$15", id = 12)
            )
        ),
        ServiceType(
            serviceTypeHeading = "Spa Services",
            services = listOf(
                Service("Body Spa", price = "$50", id = 13),
                Service("Hair Spa", price = "$30", id = 14),
                Service("Foot Spa", price = "$15", id = 15)
            )
        ),
        ServiceType(
            serviceTypeHeading = "Waxing Services",
            services = listOf(
                Service("Full Body Waxing", price = "$50", id = 16),
                Service("Half Body Waxing", price = "$30", id = 17),
                Service("Underarms Waxing", price = "$15", id = 18)
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 6.dp, end = 6.dp, top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(serviceTypes) { serviceType ->
                ServiceTypeItem(serviceType = serviceType) { service,isSelected->
                    if (isSelected) {
                        /*List of services selected by the are stored here*/
                        selectedServices = selectedServices + service
                    } else {
                        selectedServices = selectedServices.filter { it.id != service.id }
                    }
                }
            }
        }
        Button(
            onClick = {
            navController.navigate(Screenes.SelecterScr.route )
        }) {
            Text(text = "Next")
        }
    }
}

@Composable
fun ServiceTypeItem(
    serviceType: ServiceType,
    onServiceSelectedChange: (Service,Boolean) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp)
    ) {
        Text(
            text = serviceType.serviceTypeHeading,
            style = MaterialTheme.typography.bodyMedium
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
                        onServiceSelectedChange(service,isChecked)
                    }
                )
                Text(
                    text = service.serviceName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServicesScreenPreview() {
    val navController = rememberNavController()
    ServiceSelectorScreen(navController)
}