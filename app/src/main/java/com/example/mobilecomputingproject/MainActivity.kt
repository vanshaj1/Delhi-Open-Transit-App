package com.example.mobilecomputingproject

import android.content.Context
//import dev.chrisbanes.accompanist.coil.CoilImage
import android.content.Intent
//import android.icu.number.Scale
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.em
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
                    color = MaterialTheme.colorScheme.background,
//                    color = Color.Black
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
        color = Color.Black,
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
            Row {
                OutlinedButton(onClick = {
                    val intent = Intent(context, MainActivity3::class.java)
                    context.startActivity(intent)
                },
                    modifier = Modifier.height(50.dp).width(150.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.5.dp,Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "", tint = Color.Black)
                    Text(text = "YES",color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.12.em)
                }
                Spacer(modifier = Modifier.width(20.dp))
                OutlinedButton(onClick = {
                    val intent = Intent(context, MainActivity2::class.java)
                    context.startActivity(intent)
                },
                    modifier = Modifier.height(50.dp).width(150.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.5.dp,Color.Black)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "", tint = Color.Black)
                    Text(text = "NO",color = Color.Black, fontSize = 19.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.12.em)
                }
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