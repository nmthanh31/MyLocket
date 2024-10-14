package com.nmthanh31.mylocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.nmthanh31.mylocket.navigation.MyLocketNavHost
import com.nmthanh31.mylocket.ui.screens.ChatScreen
import com.nmthanh31.mylocket.ui.screens.HomeScreen
import com.nmthanh31.mylocket.ui.theme.MyLocketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MyLocketTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
//                    WelcomeScreen(modifier = Modifier.fillMaxSize())
//                    RegisterAndLoginScreen(modifier = Modifier.fillMaxSize().padding(paddingValues))
//                    ChoosePasswordScreen(modifier = Modifier.fillMaxSize().padding(paddingValues))
//                    HomeScreen(modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues))
//                    ProfileScreen()
//                    ChatScreen()
                    MyLocketNavHost()
                }

            }
        }
    }
}
