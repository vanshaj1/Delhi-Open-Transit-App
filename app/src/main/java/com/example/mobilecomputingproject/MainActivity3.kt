package com.example.mobilecomputingproject

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.mobilecomputingproject.ui.theme.MobileComputingProjectTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class MainActivity3 : ComponentActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location by mutableStateOf("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileComputingProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

                    checkLocationPermission()
                    Greeting3(location)
                }
            }
        }
    }

    private fun checkLocationPermission(){
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                location = "Latitude: ${it.latitude}, Longitude: ${it.longitude}"
//                Text(text = (it.latitude).toString())
            }
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 100 // Update interval in milliseconds
            fastestInterval = 50 // Fastest update interval in milliseconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                this@MainActivity3.location = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove location updates when the activity is destroyed
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}

@Composable
fun Greeting3(location: String, modifier: Modifier = Modifier) {
    Text(
        text = if (location.isNotBlank()) location else "Location not available",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    MobileComputingProjectTheme {
        Greeting3("Android")
    }
}