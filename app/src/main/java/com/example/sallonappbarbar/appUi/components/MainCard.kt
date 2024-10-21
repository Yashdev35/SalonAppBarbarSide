package com.example.sallonappbarbar.appUi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.R

@Composable
fun DoubleCard(
//    title: String, *Instead using topApp bar composable to add any thing at top
//    onBackClick: () -> Unit,
    midCarBody: @Composable () -> Unit,
    navController: NavController = rememberNavController(),
    mainScreen: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit = {},
    bottomAppBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.purple_200)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        topAppBar()
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            backgroundColor = colorResource(id = R.color.sallon_color)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                midCarBody()
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
//                        .verticalScroll(scrollState)
                    ,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    backgroundColor = colorResource(id = R.color.white)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        mainScreen()
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .zIndex(1f) // Ensure the BottomAppBar is on top
                        ) {
                            bottomAppBar()
                        }
                    }
                }
            }
        }
    }
}