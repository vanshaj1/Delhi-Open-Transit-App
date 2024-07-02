@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.example.finalproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalproject.database.Combined
import com.example.finalproject.database.DatabaseContract
import com.example.finalproject.database.DatabaseHelper
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.util.PriorityQueue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val customFontFamily = FontFamily(Font(R.font.kanit))
var longitude by mutableStateOf(0.0)
var latitude by mutableStateOf(0.0)

class MainActivity : ComponentActivity() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dbHelper = DatabaseHelper(this)
                    val buses = dbHelper.getBusStops()
                    val stopCoordinates = dbHelper.getStopCoordinates()
                    val busStops: HashMap<String, ArrayList<String>> = hashMapOf()
                    for ((entityId, routeMap) in buses) {
                        for ((_, stopsList) in routeMap) {
                            for (stop in stopsList) {
                                if (busStops.containsKey(entityId)) {
                                    busStops[entityId]?.add(stop)
                                } else {
                                    busStops[entityId] = arrayListOf(stop)
                                }
                            }
                        }
                    }
                    val stopsList = getStopsData(dbHelper)
                    val stopsNames = getStops(dbHelper)
                    val navController = rememberNavController()
                    getLocation()
                    MyApp(navController, stopsList = stopsList, buses = buses, stopsNames = stopsNames, busStops = busStops, stopCoordinates = stopCoordinates)
                }

            }
        }
    }
    fun getLocation(){

        if(isLocationPermissionsGranted()){

            if(isLocationSettingsEnabled()){

                if (checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }

//citation:- https://blog.mindorks.com/using-gps-location-manager-in-android-android-tutorial/, https://www.youtube.com/watch?v=GZ5G16JvDpc
                // For current immediate Location on limited duration,
                val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                latitude = location.latitude
                                longitude = location.longitude
                            }
                        }
                    },
                    Looper.myLooper()
                )


            }else{

//                Open Settings for Location turning on
                Toast.makeText(applicationContext,"Please, Turn On Location", Toast.LENGTH_SHORT).show()
                val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent)

            }
        }else{

            askLocationPermission()
        }
    }


    companion object{
        private const val LOCATION_ACCESS_REQUEST = 100
    }




    fun isLocationSettingsEnabled():Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }





    fun isLocationPermissionsGranted(): Boolean{

        if(
            checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            &&
            checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }




    fun askLocationPermission(){
        requestPermissions(this,arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
            LOCATION_ACCESS_REQUEST  )
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_ACCESS_REQUEST){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            }else{
                Toast.makeText(applicationContext,"Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun MyApp(navController: NavHostController, stopsList: List<Combined>, buses: HashMap<String, HashMap<String, ArrayList<String>>>, stopsNames: List<String>, busStops: HashMap<String, ArrayList<String>>, stopCoordinates: HashMap<String, Pair<Double, Double>>) {
    NavHost(navController, startDestination = "loading") {
        composable("loading") {
            LoadingScreen(navController = navController)
        }
        composable("home") {
            HomePage(navController = navController)
        }
        composable("travelling") {
            Travelling(navController = navController)
        }
        composable("stopsList") {
            StopsList(stopsList = stopsList)
        }
        composable("busList") {
            BusesList(navController = navController, buses = buses)
        }
        composable("search") {
            Search(navController = navController, buses = buses, stopsNames = stopsNames)
        }
        composable("sensor") {
            BusTravelInputScreen(navController = navController, busStops = busStops, stopCoordinates = stopCoordinates)
        }
        composable(
            route = "matchingBusesScreen/{source}/{destination}/{matchingBuses}",
            arguments = listOf(
                navArgument("source") { type = NavType.StringType },
                navArgument("destination") { type = NavType.StringType },
                navArgument("matchingBuses") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source") ?: ""
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val matchingBusesString = backStackEntry.arguments?.getString("matchingBuses")
            val matchingBusesList = matchingBusesString?.split(",") ?: emptyList()

            MatchingBusesScreen(navController, stopsList, matchingBusesList, source, destination, buses)
        }
        composable(
            route = "stopsScreen/{source}/{destination}/{stops}",
            arguments = listOf(
                navArgument("source") { type = NavType.StringType },
                navArgument("destination") { type = NavType.StringType },
                navArgument("stops") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val stopsString = backStackEntry.arguments?.getString("stops")
            val stopsList = stopsString?.split(",") ?: emptyList()
            Log.d("myTag", "Value: $route")
            MatchingStopsScreen(navController, stopsList)
        }
        composable(
            route = "sensorStops/{nearestStop}/{busRegistration}/{destinationStop}",
            arguments = listOf(
                navArgument("nearestStop") { type = NavType.StringType },
                navArgument("busRegistration") { type = NavType.StringType },
                navArgument("destinationStop") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val nearestStop = navBackStackEntry.arguments?.getString("nearestStop")
            val busRegistration = navBackStackEntry.arguments?.getString("busRegistration")
            val destinationStop = navBackStackEntry.arguments?.getString("destinationStop")
            SensorStops(
                navController = navController,
                nearestStop = nearestStop!!,
                busRegistration = busRegistration!!,
                destinationStop = destinationStop!!,
                busStops = busStops
            )
        }
    }
}

@Composable
fun LoadingScreen(navController: NavHostController) {
    val isLoadingComplete = remember { mutableStateOf(false) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(2000)
        scale.animateTo(1.5f, animationSpec = tween(durationMillis = 1000))
        scale.animateTo(0f, animationSpec = tween(durationMillis = 1000))
        delay(1000)
        isLoadingComplete.value = true
    }

    if (isLoadingComplete.value) {
        navController.navigate("home")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bus_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun HomePage(navController: NavController) {
    Surface(color = Color.White) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.loading_bg),
                contentDescription = "Background Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp)
                    .scale(scaleX = 1f, scaleY = 1f),
                contentScale = ContentScale.FillBounds)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Find  your\nBus route",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = customFontFamily)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    modifier = Modifier
                        .width(65.dp)
                        .height(90.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE83A30),
                        contentColor = Color.White
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Navigate to Bus Search",
                        tint = Color.White
                    )
                }

            }
        }
    }
}

@Composable
fun Travelling(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading_bg),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 200.dp),
            contentScale = ContentScale.FillBounds)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Are you currently\ntravelling?",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = customFontFamily),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navController.navigate("sensor") },
                    modifier = Modifier.size(width = 120.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE83A30),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Yes")
                }
                Button(
                    onClick = { navController.navigate("search") },
                    modifier = Modifier.size(width = 120.dp, height = 50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE83A30),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "No")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavHostController, buses: HashMap<String, HashMap<String, ArrayList<String>>>, stopsNames: List<String>) {
    var source by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var matchingBuses by remember { mutableStateOf<List<String>>(emptyList()) }
    var showMessage by remember { mutableStateOf(false) }
    if (showMessage) {
        Snackbar(
            action = {
                Button(onClick = { showMessage = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text("Incorrect Input")
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Row(modifier = Modifier
            .padding(start = 12.dp, end = 12.dp, top = 16.dp)
            .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    navController.navigate("busList")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ){
                Icon(imageVector = Icons.Filled.Menu,
                    contentDescription = "menu",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navController.navigate("travelling")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "home",
                    tint = Color(0xFFE83A30)
                )
            }
        }
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Spacer(modifier = Modifier.height(36.dp))
            Column(modifier = Modifier.padding(start = 24.dp)) {
                Text(
                    text = "Go easily\nwhere you want\nto",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = customFontFamily),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFFE83A30))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Row (modifier = Modifier.height(26.dp)){
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = "Location icon",
                                    tint = Color.White
                                )
                                Text(
                                    text = "From",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            TextField(
                                value = source,
                                onValueChange = { source = it },
                                label = { Text("Select Location ") },
                                modifier = Modifier
                                    .height(50.dp)
                                    .padding(start = 24.dp),
                                textStyle = TextStyle(color = Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    cursorColor = Color.White,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))
                        Column {
                            Text(
                                text = "--------------------------------------------------------",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE6D66))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = "swap",
                                tint = Color.Black,
                                modifier = Modifier.aspectRatio(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Row (modifier = Modifier.height(26.dp)){
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = "Location icon",
                                    tint = Color.White
                                )
                                Text(
                                    text = "To",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            TextField(
                                value = destination,
                                onValueChange = { destination = it },
                                label = { Text("Select Destination ") },
                                modifier = Modifier
                                    .height(50.dp)
                                    .padding(start = 24.dp),
                                textStyle = TextStyle(color = Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    cursorColor = Color.White,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.map_bg),
                    contentDescription = "map screenshot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(64.dp))
                )
                Button(
                    onClick = {
                        matchingBuses = findBusesWithStops(source = source, destination = destination, buses = buses)
                        if(matchingBuses.isEmpty()){
                            matchingBuses = findBusesWithStops2(source = source, destination = destination, buses = buses)
                            if(matchingBuses.isEmpty()){
                                showMessage = true
                            }
                            else{
                                navController.navigate("matchingBusesScreen/${source}/${destination}/${matchingBuses.joinToString(",")}")
                            }
                        }
                        //DL1PD2501 Shakarpur - Gazipur Depot, DL1PC9343 Gazipur Depot - ISBT Anand Vihar
                        // Shakarpur-Shakarpur Crossing-Nirman-Sawarthya-New Rajdhani-KarkarDooma-HassanpurDepot-HassanpurVillage-Gazipur-ISBT
                        // DL1PC9140 DL1PC8072 DL1PC7760 DL1PC9145 DL1PC7135
                        else{
                            showMessage = false
                            navController.navigate("matchingBusesScreen/${source}/${destination}/${matchingBuses.joinToString(",")}")
                        }
                              },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .size(height = 50.dp, width = 120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE83A30),
                        contentColor = Color.White
                    )
                ) {
                    Text("Search")
                }
            }
        }
        if (showMessage) {
            Snackbar(
                action = {
                    Button(onClick = { showMessage = false }) {
                        Text("Dismiss")
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Incorrect Bus Stops")
            }
        }
    }
}


@Composable
fun BusesList(navController: NavController, buses: HashMap<String, HashMap<String, ArrayList<String>>>) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("busList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "busesInfo",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Bus Stops",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    val busList = buses.entries.toList()
                    items(busList) { busEntry ->
                        val busNumber = busEntry.key
                        val routeStopsMap = busEntry.value
                        BusStopGroup(busNumber, routeStopsMap)
                    }
                }
            }
        }
    }
}


@Composable
fun BusStopGroup(busNumber: String, routeStopsMap: HashMap<String, ArrayList<String>>) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)) {
        Text(
            text = "Bus No.: $busNumber",
            style = MaterialTheme.typography.headlineSmall
        )

        routeStopsMap.forEach { (routeName, stops) ->
            val busStopsString = StringBuilder()
            stops.forEach { stop ->
                busStopsString.append(stop)
                busStopsString.append(", ")
            }
            Text(
                text = "Route: $routeName\nStops:",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(text = "\n{$busStopsString}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusTravelInputScreen(navController: NavHostController, busStops: HashMap<String, ArrayList<String>>, stopCoordinates: HashMap<String, Pair<Double, Double>>) {
    var busRegistration by remember { mutableStateOf("") }
    var destinationStop by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    val light = Color(0x20000000)
    if (showMessage) {
        Snackbar(
            action = {
                Button(onClick = { showMessage = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text("Incorrect Input")
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("busList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.loading_bg),
                contentDescription = "Background Image",
                modifier = Modifier,
                contentScale = ContentScale.FillBounds)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
//                TextField(
//                    value = busRegistration,
//                    onValueChange = { busRegistration = it },
//                    label = { Text("Bus Number you're travelling in") },
//                    modifier = Modifier.fillMaxWidth(),
//                    textStyle = TextStyle(color = Color.Black),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        cursorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Transparent
//                    )
//                )
                TextField(
//                    value = busRegistration,
//                    onValueChange = { busRegistration = it },
//                    label = { Text("Bus Number you're travelling in") },
//                    modifier = Modifier.fillMaxWidth(),
//                    textStyle = TextStyle(color = Color.Black),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        cursorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Transparent
//                    )
                    modifier = Modifier.width(300.dp),
                    value = busRegistration,
                    placeholder = { Text("Bus Number you're travelling in",color = Color.Black) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = light,
                        cursorColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        busRegistration = it
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
//                TextField(
//                    value = destinationStop,
//                    onValueChange = { destinationStop = it },
//                    label = { Text("Your Destination Stop") },
//                    modifier = Modifier.fillMaxWidth(),
//                    textStyle = TextStyle(color = Color.Black),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        cursorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Transparent
//                    )
//                )
                TextField(
//                    value = destinationStop,
//                    onValueChange = { destinationStop = it },
//                    label = { Text("Your Destination Stop") },
//                    modifier = Modifier.fillMaxWidth(),
//                    textStyle = TextStyle(color = Color.Black),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        cursorColor = Color.Black,
//                        unfocusedIndicatorColor = Color.Transparent
//                    )
                    modifier = Modifier.width(300.dp),
                    value = destinationStop,
                    placeholder = { Text("Your Destination Stop",color = Color.Black) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = light,
                        cursorColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        destinationStop = it
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val nearestStop = findNearestStop(
                        busStops,
                        stopCoordinates,
                        busRegistration,
                        latitude,
                        longitude
                    )
                    if(nearestStop.isNullOrEmpty()){
                        showMessage = true
                    }
                    else{
                        showMessage = false
                        navController.navigate("sensorStops/$nearestStop/$busRegistration/$destinationStop")
                    }
                                 },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE83A30),
                        contentColor = Color.White
                    )) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = Color.White)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Search Bus", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        if (showMessage) {
            Snackbar(
                action = {
                    Button(onClick = { showMessage = false }) {
                        Text("Dismiss")
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Incorrect Details")
            }
        }
    }
}

fun findNearestStop(
    busStops: HashMap<String, ArrayList<String>>,
    stopCoordinates: HashMap<String, Pair<Double, Double>>,
    busNumber: String,
    latitude: Double,
    longitude: Double
): String? {
    val stopsForBus = busStops[busNumber]
    stopsForBus?.let { stops ->
        var nearestStop: String? = null
        var minDistance = Double.MAX_VALUE

        for (stop in stops) {
            val stopCoordinatesPair = stopCoordinates[stop]
            stopCoordinatesPair?.let { pair ->
                val stopLat = pair.first
                val stopLon = pair.second

                val distance = calculateDistance(latitude, longitude, stopLat, stopLon)
                if (distance < minDistance) {
                    minDistance = distance
                    nearestStop = stop
                }
            }
        }
        return nearestStop
    }
    return null
}

private fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val radius = 6371
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = (sin(latDistance / 2) * sin(latDistance / 2)
            + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
            * sin(lonDistance / 2) * sin(lonDistance / 2)))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

@Composable
fun NearestStopScreen(navController: NavController, nearestStop: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("busList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Nearest Stop: $nearestStop")
            }
        }
    }
}


@Composable
fun StopsList(stopsList: List<Combined>) {
    Column {
        Text(
            text = "Database",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(stopsList) { stop ->
                StopRow(stop)
            }
        }
    }
}

@Composable
fun StopRow(stop: Combined) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp)){
        Text("Bus Registration No.: ${stop.entityId}")
        Text("Route: ${stop.routeName}")
        Text("Stop Name: ${stop.stopName}")
        Text("Stop Number: ${stop.stopSequence}")
        Spacer(modifier = Modifier.height(24.dp))
    }
}

fun getStopsData(dbHelper: DatabaseHelper): List<Combined> {
    val db = dbHelper.readableDatabase
    val stopsList = mutableListOf<Combined>()
//    val limit = 500
    val cursor = db.query(
        DatabaseContract.CombinedEntry.TABLE_NAME,
        null,
        null,
        null,
        null,
        null,
        null,
//        "$limit"
    )
    with(cursor) {
        while (moveToNext()) {
            val registrationNumber = getString(getColumnIndexOrThrow(DatabaseContract.CombinedEntry.COLUMN_ENTITY_ID))
            val routeName = getString(getColumnIndexOrThrow(DatabaseContract.CombinedEntry.COLUMN_ROUTE_LONG_NAME))
            val stopName = getString(getColumnIndexOrThrow(DatabaseContract.CombinedEntry.COLUMN_STOP_NAME))
            val stopSequence = getInt(getColumnIndexOrThrow(DatabaseContract.CombinedEntry.COLUMN_STOP_SEQUENCE))
            stopsList.add(Combined(registrationNumber, routeName, stopName, stopSequence))
        }
    }
    cursor.close()
    return stopsList
}

fun getStops(dbHelper: DatabaseHelper): List<String> {
    val db = dbHelper.readableDatabase
    val stopsList = mutableListOf<String>()
    val cursor = db.query(
        DatabaseContract.CombinedEntry.TABLE_NAME,
        null,
        null,
        null,
        null,
        null,
        null,
//        "$limit"
    )
    with(cursor) {
        while (moveToNext()) {
            val stopName = getString(getColumnIndexOrThrow(DatabaseContract.CombinedEntry.COLUMN_STOP_NAME))
            stopsList.add(stopName)
        }
    }
    cursor.close()
    return stopsList
}

fun findBusesWithStops(source: String, destination: String, buses: HashMap<String, HashMap<String, ArrayList<String>>>): List<String> {
    val matchingBuses = mutableListOf<String>()
    for ((busNumber, routeStopsMap) in buses) {
        if (routeStopsMap.any { (_, stops) ->
                stops.contains(source) && stops.contains(destination)
            }) {
            matchingBuses.add(busNumber)
        }
    }
    return matchingBuses
}

fun findBusesWithStops2(source: String, destination: String, buses: HashMap<String, HashMap<String, ArrayList<String>>>): List<String> {
    val graph = buildGraph(buses)
    val path = dijkstra(graph, source, destination)
    return path ?: emptyList()
}

fun buildGraph(buses: HashMap<String, HashMap<String, ArrayList<String>>>): Map<String, Map<String, List<String>>> {
    val graph = mutableMapOf<String, MutableMap<String, List<String>>>()
    for ((busNumber, routeStopsMap) in buses) {
        for ((routeName, stops) in routeStopsMap) {
            for (i in 0 until stops.size - 1) {
                val start = stops[i]
                val end = stops[i + 1]
                graph.getOrPut(start) { mutableMapOf() }[end] = listOf(busNumber, routeName)
            }
        }
    }
    return graph
}

fun dijkstra(graph: Map<String, Map<String, List<String>>>, source: String, destination: String): List<String>? {
    val distances = mutableMapOf<String, Int>()
    val prev = mutableMapOf<String, Pair<String, List<String>>>()
    val queue = PriorityQueue<Pair<String, List<String>>>(compareBy { it.second.size })

    distances[source] = 0
    queue.add(source to emptyList())

    while (queue.isNotEmpty()) {
        val (current, path) = queue.poll()!!
        if (current == destination) {
            return path
        }
        val currentDist = distances[current] ?: continue
        for ((neighbor, busInfo) in graph[current] ?: emptyMap()) {
            val newDist = currentDist + 1
            if (distances[neighbor] == null || newDist < distances[neighbor]!!) {
                distances[neighbor] = newDist
                prev[neighbor] = current to busInfo
                queue.add(neighbor to (path + busInfo))
            }
        }
    }
    return null
}


@Composable
fun MatchingBusesScreen(navController: NavHostController, stopsList: List<Combined>, matchingBuses: List<String>, source: String,
                        destination: String, buses: HashMap<String, HashMap<String, ArrayList<String>>>) {
    val lightBlue = Color(0xFFFFC0CB)
    val calculateStops: (String, String) -> List<String> = { entityId, routeName ->
        val busInfo = buses[entityId]
        if (busInfo != null) {
            val routeInfo = busInfo[routeName]
            if (routeInfo != null) {
                val sourceIndex = routeInfo.indexOf(source)
                val destinationIndex = routeInfo.indexOf(destination)
                if (sourceIndex != -1 && destinationIndex != -1) {
                    try {
                        val stopsInRange = if (sourceIndex <= destinationIndex) {
                            routeInfo.subList(sourceIndex, destinationIndex + 1)
                        } else {
                            val stopsInReverseRange = routeInfo.subList(destinationIndex, sourceIndex + 1)
                            stopsInReverseRange.reversed()
                        }
                        stopsInRange
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emptyList()
                    }
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    
    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("search")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "menu",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Spacer(modifier = Modifier.height(36.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Available Buses",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontFamily = customFontFamily),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                val entityRouteList = stopsList
                    .filter { matchingBuses.contains(it.entityId) }
                    .map { it.entityId to it.routeName }.toSet().toList()

                LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                    items(entityRouteList) { (entityId, routeName) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    val stops = calculateStops(entityId, routeName)
                                    Log.d("MyTag", "The value of stops is: $stops")
                                    val stopsEncoded = stops.map { URLEncoder.encode(it, "UTF-8") }
                                    val myString = "stopsScreen/${source}/${destination}/${
                                        stopsEncoded.joinToString(
                                            ","
                                        )
                                    }"
                                    Log.d("MyTag", "The value of route is: $myString")
                                    navController.navigate(myString)
                                },
                        ) {
//                            Column(
//                                modifier = Modifier
//                                    .padding(horizontal = 16.dp)
//                            ) {
//                                Text(
//                                    text = "Route Name:\t$routeName",
//                                    style = TextStyle(
//                                        fontSize = 16.sp,
//                                        color = LocalContentColor.current,
//                                        lineHeight = 24.sp
//                                    )
//                                )
//                                Spacer(modifier = Modifier.height(5.dp))
//                                Text(
//                                    text = "Bus Number:\t$entityId",
//                                    style = TextStyle(
//                                        fontSize = 16.sp,
//                                        color = LocalContentColor.current,
//                                        lineHeight = 24.sp
//                                    )
//                                )
//                            }
                            Box(
                                modifier = Modifier
                                    .width(350.dp)
                                    .background(lightBlue, shape = RoundedCornerShape(20.dp))
                                    .padding(18.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.iconbus),
                                        contentDescription = "Bus Icon",
                                        tint = Color.Black,
                                        modifier = Modifier.size(34.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Column {
                                        Text(text = "Route Name: $routeName", fontSize = 20.sp)
                                        Text(text = "Bus Number: $entityId", fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun MatchingStopsScreen(navController: NavHostController, stopsList: List<String>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(modifier = Modifier
                .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("busList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ){
                    Icon(imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Bus Stops",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = customFontFamily),
                modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 24.dp, vertical = 16.dp
                    )
            ) {
                items(stopsList.size) { stop ->
//                    Card (modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp)
//                    ){
//                        Column (modifier = Modifier.padding(vertical = 16.dp)){
//                            Text(
//                                text = "Stop: ${stop.replace("+", " ")}",
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp),
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    color = LocalContentColor.current,
//                                    lineHeight = 24.sp
//                                )
//                            )
//                        }
//                    }
                    Row {
                        if(stop == 0){
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color(0xFF00CD00),modifier = Modifier.size(34.dp))
                        }
                        else if(stop == stopsList.size-1){
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Red,modifier = Modifier.size(34.dp))
                        }
                        else{
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                        }
//                        Icon(painter = painterResource(id = R.drawable.iconbus), contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                        Text(text = stopsList[stop].replace("+", " "), fontSize = 20.sp)
                    }
                    if(stop!=stopsList.size-1){
                        Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                        Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
fun SensorStops(navController: NavController, nearestStop: String, busRegistration: String, destinationStop: String, busStops: HashMap<String, ArrayList<String>>){
    val LightRed = Color(0xFFE83A30)
    val customHeading = androidx.compose.ui.text.TextStyle(
        fontSize = 34.sp,
        color = LightRed,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W900
    )
    val LightBlue = Color(0xFF0000CD)
    val customSubHeading = androidx.compose.ui.text.TextStyle(
        fontSize = 24.sp,
        color = LightBlue,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W900
    )
    val stopsList = busStops[busRegistration] ?: emptyList()
    val nearestStopIndex = stopsList.indexOf(nearestStop)
    val destinationStopIndex = stopsList.indexOf(destinationStop)
    var showMessage by remember { mutableStateOf(false) }
    val slicedStopsList = if (nearestStopIndex != -1 && destinationStopIndex != -1 && nearestStopIndex < destinationStopIndex) {
        stopsList.subList(nearestStopIndex, destinationStopIndex + 1)
    } else {
        emptyList()
    }
    if (showMessage) {
        Snackbar(
            action = {
                Button(onClick = { showMessage = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text("You reached to your destination")
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 16.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.navigate("busList")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "menu",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        navController.navigate("travelling")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "home",
                        tint = Color(0xFFE83A30)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Nearest Stop\n",style=customHeading,modifier = Modifier.padding(bottom = 38.dp))
                Row{
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(28.dp))
                    Text(text = "$nearestStop",style=customSubHeading)
                }
                if(nearestStop == destinationStop){
                    showMessage = true
                }
            }
            if(slicedStopsList.isEmpty()){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                ){
                    Text("You're in the wrong bus, your destination is not on this route")
                }
            }
            else{
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .background(Color.White)) {
                    Text(
                        text = "Bus Stops Ahead",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = customFontFamily),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    items(slicedStopsList.size) { stop ->
//                        Text(
//                            text = stop,
//                            modifier = Modifier.padding(16.dp),
//                            fontSize = 16.sp
//                        )

//                        Row {
//                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
//                            Text(text = slicedStopsList[stop].toString(), fontSize = 20.sp)
//                        }
                        Row {
                            if(stop == 0){
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color(0xFF00CD00),modifier = Modifier.size(34.dp))
                            }
                            else if(stop == 1){
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color(0xFFFFA500),modifier = Modifier.size(34.dp))
                            }
                            else if(stop == slicedStopsList.size-1){
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Red,modifier = Modifier.size(34.dp))
                            }
                            else{
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                            }
//                        Icon(painter = painterResource(id = R.drawable.iconbus), contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                            Text(text = slicedStopsList[stop].toString(), fontSize = 20.sp)
                        }
                        if(stop!=slicedStopsList.size-1){
                            Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                            Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BusDataSearch(navController: NavController, ){

}