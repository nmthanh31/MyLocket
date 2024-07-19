package com.nmthanh31.mylocket.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.*

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = RoundedCornerShape(12.dp)),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Locket",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(
                text = "Ảnh trực tiếp từ bạn bè,",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB7B7B7),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ngay trên màn hình chính",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB7B7B7),
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber,
                contentColor = Amber
            ),
            modifier = Modifier.height(60.dp)
        ) {
            Text(
                text = "Tạo một tài khoản",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall
                )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Đăng nhập",
            modifier = Modifier.clickable {  },
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}