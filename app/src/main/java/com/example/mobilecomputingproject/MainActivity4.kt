package com.example.mobilecomputingproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecomputingproject.ui.theme.MobileComputingProjectTheme

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileComputingProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting4()
                }
            }
        }
    }
}

@Composable
fun Greeting4() {
    val stops = listOf<String>("A","B","C","D","E")
    val customHeading = androidx.compose.ui.text.TextStyle(
        fontSize = 24.sp,
        color = Color.Black,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W900
    )
    Box(modifier = Modifier.fillMaxSize(),
    ){
        Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Route", style = customHeading)
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn {
                items(stops.size) { index ->
                    Row {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
//                        Icon(painter = painterResource(id = R.drawable.iconbus), contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                        Text(text = stops[index].toString(), fontSize = 26.sp)
                    }
                    if(index!=stops.size-1){
                        Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                        Text(text = "|",modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    MobileComputingProjectTheme {
        Greeting4()
    }
}