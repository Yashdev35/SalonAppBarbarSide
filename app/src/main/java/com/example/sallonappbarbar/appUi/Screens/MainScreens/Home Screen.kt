package com.example.sallonappbarbar.appUi.Screens.MainScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sallonappbarbar.appUi.viewModel.BarberDataViewModel
import com.example.sallonappbarbar.data.Resource
import com.practicecoding.sallonapp.appui.components.CommonDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavController,
    viewModel: BarberDataViewModel = hiltViewModel(),
){
    var isBarberShopOpen by remember { mutableStateOf(false) }
    var isOpenOrClose by remember { mutableStateOf("Open") }
    var isLoading by remember { mutableStateOf(false) }
    if(isLoading) {
        CommonDialog()
    }
    val scope = rememberCoroutineScope()
    scope.launch(Dispatchers.IO) {
        viewModel.getBarberData(activity).collect(){resource ->
            when(resource) {
                is Resource.Success -> {
                    isLoading = false
                    Toast.makeText(activity, "Data Loaded", Toast.LENGTH_SHORT).show()
                    val BarberData = resource.result
                    if(BarberData.isShopOpen!!.lowercase() == "yes") {
                        isBarberShopOpen = true
                        isOpenOrClose = "Open"
                    } else {
                        isBarberShopOpen = false
                        isOpenOrClose = "Close"
                    }
                }
                is Resource.Failure -> {
                    isLoading = false
                    Toast.makeText(activity, resource.exception.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                     isLoading = true
                }
            }
        }
    }


    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Barber Shop",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$isOpenOrClose",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Switch(
                    checked = isBarberShopOpen,
                    onCheckedChange = { isChecked ->
                        isBarberShopOpen = isChecked
                        if (isChecked) {
                            scope.launch(Dispatchers.IO) {
                                viewModel.isShopOpen(true, activity)
                            }
                        } else {
                            scope.launch(Dispatchers.IO) {
                                viewModel.isShopOpen(false, activity)
                            }
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.White,
                    )
                )
            }
        }
    }
}