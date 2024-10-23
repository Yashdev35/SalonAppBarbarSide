package com.example.sallonappbarbar

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.navigation.compose.rememberNavController
import com.example.sallonappbarbar.appUi.navigation.AppNavigation
import com.example.sallonappbarbar.ui.theme.SallonAppbarbarTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SallonAppbarbarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController = navController)
                }
            }
            checkAndEnableLocation() // Ensure location is enabled on start
        }
    }

    // Check and enable location services
    private fun checkAndEnableLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!isLocationEnabled()) {
                showEnableLocationDialog() // Show dialog to enable location
            } else {
                prepLocationUpdates() // Prepare location updates if location is already enabled
            }
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Check if location services are enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Show dialog to enable location, making sure the user can't bypass it
    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Enable Location")
            setMessage("Location access is mandatory to show nearby barbers. Please enable location services.")
            setCancelable(false) // Non-cancelable dialog
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            create()
            show()
        }
    }

    // Prepare location updates if permission is granted and location is enabled
    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Request permission launcher to get location permission from the user
    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestLocationUpdates() // If permission is granted, start location updates
            } else {
                Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
            }
        }

    // Request location updates (to be implemented)
    private fun requestLocationUpdates() {
        // locationViewModel.startLocationUpdates()
    }

    // On returning to the app, re-check if location is enabled
    override fun onResume() {
        super.onResume()
        if (!isLocationEnabled()) {
            showEnableLocationDialog() // Show dialog again if location is still disabled
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    SallonAppbarbarTheme {
        Surface {
            Text(text = "Hello, World!")
        }
    }
}