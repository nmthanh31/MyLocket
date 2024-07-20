package com.nmthanh31.mylocket.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.Amber
import com.nmthanh31.mylocket.ui.theme.Background
import com.nmthanh31.mylocket.ui.theme.Charcoal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePasswordScreen(modifier: Modifier = Modifier){

    var password by remember {
        mutableStateOf("")
    }

    val isTruePassword = remember (password) {
        password.length >= 8
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
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
                painter = painterResource(id = R.drawable.back),
                contentDescription = null
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Chọn một mật khẩu",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,

            )

            TextField(
                value = password,
                onValueChange = {input ->
                    run {
                        password = input
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Charcoal,
                    focusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color.White
                ),
                placeholder = {
                    Text(text = "Mật khẩu")
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 15.dp, bottom = 15.dp)
                    .clip(shape = RoundedCornerShape(10.dp))

            )

            Row(

            ) {
                Text(
                    text = "Mật khẩu phải có ít nhất ",
                    color = Color.White
                )
                Text(
                    text = "8 ký tự",
                    color = Amber
                )
            }
        }

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber,
                contentColor = Color.Black,
                disabledContainerColor = Charcoal,
                disabledContentColor = Color(0xFF4E4E50)
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp),
            enabled = isTruePassword
        ) {
           Text(
               text = "Tiếp tục",
               fontWeight = FontWeight.Bold
           )
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
                tint = if (isTruePassword) Color.Black else Color(0xFF4E4E50)
            )
        }
    }
}

@Preview
@Composable
fun PreviewChoosePasswordScreen(){
    ChoosePasswordScreen()
}