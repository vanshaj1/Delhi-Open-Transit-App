package com.example.mobilecomputingproject

import android.content.Context
//import dev.chrisbanes.accompanist.coil.CoilImage
import android.content.Intent
//import android.icu.number.Scale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mobilecomputingproject.ui.theme.MobileComputingProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MobileComputingProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background,
                    color = Color.Black
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val context= LocalContext.current
    val light = Color(0x99000000)
    val customHeading = androidx.compose.ui.text.TextStyle(
        fontSize = 24.sp,
        color = Color.White,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W900
    )
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Are You Travelling ?", style = customHeading)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                val intent = Intent(context, MainActivity3::class.java)
                context.startActivity(intent)
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(width = 150.dp, height = 60.dp)
            ) {
                Text(text = "YES", fontSize = 30.sp,color= Color.Black)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = {
                val intent = Intent(context, MainActivity2::class.java)
                context.startActivity(intent)
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier.size(width = 150.dp, height = 60.dp)
            ) {
                Text(text = "NO", fontSize = 30.sp,color= Color.Black)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputingProjectTheme {
        Greeting()
    }
}