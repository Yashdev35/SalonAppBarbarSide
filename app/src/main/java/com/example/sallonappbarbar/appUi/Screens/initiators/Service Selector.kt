package com.example.sallonappbarbar.appUi.Screens.initiators


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class Service(
    val serviceName: String,
    var isServiceSelected: Boolean,
    var price: String
)

data class ServiceType(
    val serviceTypeHeading: String,
    val services: List<Service>
)

@Composable
fun ServiceCheckbox(
    service: Service,
    onServiceSelectedChange: (Service) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Checkbox(
            checked = service.isServiceSelected,
            onCheckedChange = {
                onServiceSelectedChange(service.copy(isServiceSelected = it))
            },
            modifier = Modifier
                .padding(end = 8.dp)
                .align(CenterVertically)
        )
        Text(
            text = service.serviceName,
            modifier = Modifier
                .align(CenterVertically)
                .clickable {
                    onServiceSelectedChange(service.copy(isServiceSelected = !service.isServiceSelected))
                }
        )
    }
}

@Composable
fun ServiceCategory(
    serviceType: ServiceType,
    selectedServices: MutableState<MutableList<Service>>,
    onServiceSelectedChange: (Service, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = serviceType.serviceTypeHeading,
            style = MaterialTheme.typography.bodyMedium
        )
        serviceType.services.forEach { service ->
            ServiceCheckbox(
                service = service,
                onServiceSelectedChange = { updatedService ->
                    onServiceSelectedChange(updatedService, updatedService.isServiceSelected)
                }
            )
        }
    }
}

@Composable
fun ServicesScreen() {
    var selectedServices by remember { mutableStateOf(mutableListOf<Service>()) }

    val serviceTypes = listOf(
        ServiceType(
            serviceTypeHeading = "Hair Services",
            services = listOf(
                Service("Haircut", false, "$20"),
                Service("Hair Coloring", false, "$50"),
                Service("Hair Styling", false, "$30")
            )
        ),
        ServiceType(
            serviceTypeHeading = "Nail Services",
            services = listOf(
                Service("Manicure", false, "$25"),
                Service("Pedicure", false, "$30"),
                Service("Nail Art", false, "$15")
            )
        )
        // Add more service types as needed
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(serviceTypes) { serviceType ->

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected Services: ${selectedServices.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}




@Preview(showBackground = true)
@Composable
fun ServicesScreenPreview() {
    ServicesScreen()
}