package com.nmthanh31.mylocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.nmthanh31.mylocket.ui.screens.ChoosePasswordScreen
import com.nmthanh31.mylocket.ui.screens.ChooseUsernameScreen
import com.nmthanh31.mylocket.ui.screens.HomeScreen
import com.nmthanh31.mylocket.ui.screens.RegisterAndLoginScreen
import com.nmthanh31.mylocket.ui.screens.WelcomeScreen
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
                    HomeScreen(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues))
                }

            }
        }
    }
}
