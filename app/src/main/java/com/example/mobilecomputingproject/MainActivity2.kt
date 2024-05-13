package com.example.mobilecomputingproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {

    val light = Color(0x20000000)
    val lightBlue = Color(0xFFADD8E6)

    val bus = listOf<String>("Bus 1","Bus 2","Bus 3")
    val context= LocalContext.current
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            var text1 by remember { mutableStateOf("") }
            var text2 by remember { mutableStateOf("") }

            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                modifier = Modifier.width(300.dp),
                value = text1,
                placeholder = { Text("From",color = Color.Black) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = light,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    text1 = it
                },
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                trailingIcon = {
                    if (text1.isNotEmpty()) {
                        IconButton(onClick = { text1 = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null, tint = Color.White
                            )
                        }
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.width(300.dp),
                value = text2,
                placeholder = { Text("To",color = Color.Black) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = light,
                    cursorColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    text2 = it
                },
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                trailingIcon = {
                    if (text2.isNotEmpty()) {
                        IconButton(onClick = { text2 = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = null, tint = Color.White
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedButton(onClick = { /*TODO*/ },
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )

                ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "", tint = Color.White)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Search", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(25.dp))

            val customHeading = androidx.compose.ui.text.TextStyle(
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W900
            )
            Text(text = "Available Buses", style = customHeading)
            Spacer(modifier = Modifier.height(20.dp))




            LazyColumn{
                items(bus.size) { index ->
                    Box(
                        modifier = Modifier.width(350.dp).background(lightBlue,shape = RoundedCornerShape(20.dp)).padding(18.dp),
                    ){
                        Row{
                            Icon(painter = painterResource(id = R.drawable.iconbus), contentDescription = "stop", tint = Color.Black,modifier = Modifier.size(34.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = bus[index].toString(), fontSize = 26.sp)
                            Spacer(modifier = Modifier.width(54.dp))
                            OutlinedButton(onClick = {
                                val intent = Intent(context, MainActivity4::class.java)
                                context.startActivity(intent)},
                                modifier = Modifier.height(50.dp).width(150.dp),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.5.dp,Color.Black)
                            ) {
                                Text(text = "Get Route",color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
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