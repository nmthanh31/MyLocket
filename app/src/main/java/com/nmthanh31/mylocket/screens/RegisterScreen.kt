package com.nmthanh31.mylocket.screens

import android.util.Patterns
import android.view.Display.Mode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(modifier: Modifier = Modifier){
    var email by remember {
        mutableStateOf("")
    }

    val isEmailValid = remember(email) { Patterns.EMAIL_ADDRESS.matcher(email).matches() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
    ){
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(start = 30.dp, top = 60.dp)
                .size(50.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Charcoal,
                contentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back) ,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }



        Column(
            modifier = Modifier
                .background(Background),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Email của bạn là gì?",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .padding(start = 30.dp)

            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { input ->
                    run {
                        email = input
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Charcoal,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White, // Màu con trỏ
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.White
                ),
                placeholder = {
                    Text(text = "Địa chỉ email")
                },
                singleLine = true,
            )


        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){
            Text(
                text = "Bằng cách nhấn vào nút Tiếp tục, bạn đồng ý với chúng tôi",
                color = Color(0xFF8D8A8B),
                style = MaterialTheme.typography.bodyMedium
            )
            Row {
                Text(
                    text = "Điều khoản dịch vụ",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " và ",
                    color = Color(0xFF8D8A8B),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Chính sách quyền riêng tư",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    contentColor = if (isEmailValid) Amber else Color(0xFF1F1F21),
                    containerColor = if (isEmailValid) Amber else Color(0xFF1F1F21),
                    disabledContainerColor = if (isEmailValid) Amber else Color(0xFF1F1F21)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                enabled = isEmailValid

            ) {
                Text(
                    text = "Tiếp tục",
                    color = if (isEmailValid) Color.Black else Color(0xFF4E4E50),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    tint = if (isEmailValid) Color.Black else Color(0xFF4E4E50)
                    )
            }
        }

    }
}

@Preview
@Composable
fun PreviewRegisterScreen(){
    RegisterScreen()
}
