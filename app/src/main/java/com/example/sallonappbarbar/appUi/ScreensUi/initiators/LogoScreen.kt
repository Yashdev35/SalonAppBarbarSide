package com.practicecoding.sallonapp.screens.initiatorScreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


@Composable
fun LogoScreen(
    navController: NavController,
    logoDurationMillis: Long = 2000L // Default duration of 2000 milliseconds (2 seconds)
){

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser?.uid != null) {
            val documentReference = Firebase.firestore.collection("barber")
                .document(currentUser.uid)
            try {
                val documentSnapshot = withContext(Dispatchers.IO) {
                    documentReference.get().await()
                }
                Log.d("DEBUG", "Document exists: ${documentSnapshot.exists()}")

                if (documentSnapshot.exists()) {
                    val serviceSnapshot = withContext(Dispatchers.IO) {
                        documentReference.collection("Services").get().await()
                    }
                    Log.d("DEBUG", "Service snapshot empty: ${serviceSnapshot.isEmpty}")

                    if (!serviceSnapshot.isEmpty) {
                        val slotSnapshot = withContext(Dispatchers.IO) {
                            documentReference.collection("Slots").get().await()
                        }
                        Log.d("DEBUG", "Slot snapshot empty: ${slotSnapshot.isEmpty}")

                        if (!slotSnapshot.isEmpty) {
                            navController.navigate(Screens.Home.route){
                                navController.popBackStack()
                            }

                        } else {
                            navController.navigate(Screens.SlotAdderScr.route){
                                navController.popBackStack()
                            }
                        }
                    } else {
                        navController.navigate(Screens.SelecterScr.route){
                            navController.popBackStack()
                        }
                    }
                } else {
                    navController.navigate(Screens.BarbarsSignUp.route){
                        navController.popBackStack()
                    }

                }
            } catch (e: Exception) {
                Log.e("ERROR", "Error fetching data", e)
                navController.navigate(Screens.BarbarsSignUp.route){
                    navController.popBackStack()
                }

            }
        } else {
            navController.navigate(Screens.OnBoardingScreenes.route){
                navController.popBackStack()
            }

        }

    }
    Surface(
        modifier = Modifier.fillMaxSize(),
            ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                color = colorResource(id = R.color.sallon_color)
            ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.salon_app_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .aspectRatio(1.0f).wrapContentSize().clip(shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoScreenPreview() {
    LogoScreen(navController = rememberNavController())
}
