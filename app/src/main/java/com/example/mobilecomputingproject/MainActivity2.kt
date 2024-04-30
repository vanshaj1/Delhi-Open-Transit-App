package com.example.mobilecomputingproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilecomputingproject.ui.theme.MobileComputingProjectTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileComputingProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {

    val light = Color(0x99000000)
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var text1 by remember { mutableStateOf("") }
            var text2 by remember { mutableStateOf("") }

            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = light,
                    unfocusedContainerColor = light
                ),
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("From",color = Color.White) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = light,
                    unfocusedContainerColor = light
                ),
                textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("To",color = Color.White) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MobileComputingProjectTheme {
        Greeting2("Android")
    }
}