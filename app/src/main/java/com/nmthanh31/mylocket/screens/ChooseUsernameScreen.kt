package com.nmthanh31.mylocket.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.Amber
import com.nmthanh31.mylocket.ui.theme.Background
import com.nmthanh31.mylocket.ui.theme.Charcoal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseUsernameScreen(modifier: Modifier = Modifier) {

    val usernameList = listOf("user1", "user2", "user3", "user4")

    var username by remember{
        mutableStateOf("")
    }

    val isTrueUsername = remember(username, usernameList) {
        !usernameList.contains(username) && username.length>3 && !username[0].isDigit()
    }

    val message = when {
        username.length <= 3 -> "  Phải dài hơn 3 ký tự"
        usernameList.contains(username) -> "  Rất tiếc, tên người dùng này đã có người sử dụng"
        username.isNotEmpty() && username[0].isDigit() -> "  Phải bắt đầu bằng một chữ"
        else -> "" // Trả về chuỗi rỗng nếu không có điều kiện nào thỏa mãn
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Thêm một tên người dùng",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )

            TextField(
                value = username,
                onValueChange = {
                        input -> run{
                            username = input.lowercase()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 10.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .border(
                        width = 3.dp,
                        color = if (username.isEmpty()) Charcoal else if (isTrueUsername) Charcoal else Color.Red,
                        shape = RoundedCornerShape(10.dp)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Charcoal,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                placeholder = {
                    Text(
                        text = "tên người dùng",
                        textAlign = TextAlign.Center
                    )
                },
                singleLine = true,

            )

            if (username.isEmpty()){
                Text(
                    text = "Việc này sẽ giúp bạn bè của bạn tìm thấy bạn trên Locket!",
                    color = Color(0xFF4E4E50),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp),
                    textAlign = TextAlign.Center
                )
            }else{
                if (isTrueUsername){
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                                .clip(shape = RoundedCornerShape(20.dp)),
                        )

                        Text(
                            text = "  Có sẵn!",
                            color = Amber,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }else{
                    Row(
                        modifier = Modifier.padding(start = 30.dp, end = 30.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.xcirclered),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(shape = RoundedCornerShape(20.dp)),
                        )

                        Text(
                            text = message,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }



        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber,
                contentColor = Color.Black,
                disabledContainerColor = Charcoal,
                disabledContentColor = Color(0xFF4E4E50)
            ),
            enabled = isTrueUsername
        ) {
            Text(
                text = "Tiếp tục",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun PreviewChooseUsernameScreen(){
    ChooseUsernameScreen()
}