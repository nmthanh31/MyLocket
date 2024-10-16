package com.nmthanh31.mylocket.ui.screens

import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.nmthanh31.mylocket.R

@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    navController: NavController
) {

    val currentUser = auth.currentUser

    var name = currentUser?.displayName



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(70.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier

                    .size(130.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground),
                shape = CircleShape,
                border = BorderStroke(
                    color = MaterialTheme.colorScheme.onSecondary,
                    width = 3.dp
                ),
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(120.dp)
                        .clip(shape = CircleShape)
                        .border(
                            width = 6.dp,
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }



        if (name != null) {
            InformationLine(name)
        }

        ButtonSetting(auth, navController)

    }
}

@Composable
fun InformationLine(
    name: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = name,
            color = MaterialTheme.colorScheme.secondary,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 26.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        shape = RoundedCornerShape(40.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Chia sẻ",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = "Sửa thông tin",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}


@Composable
fun ButtonSetting(
    auth: FirebaseAuth,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),

        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = "Cài đặt",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        ButtonCustom(
            title = "Thay đổi địa chỉ Email",
            icon = R.drawable.ic_email,
            status = "start",
            onAction = {})
        ButtonCustom(title = "Đăng xuất", icon = R.drawable.ic_log_out, status = "", onAction = {
            auth.signOut()
            navController.navigate("welcome")
        })
        ButtonCustom(
            title = "Xóa tài khoản",
            icon = R.drawable.ic_delele,
            status = "end",
            onAction = {})
    }
}

@Composable
fun ButtonCustom(
    title: String,
    icon: Int,
    status: String,
    onAction: () -> Unit
) {

    var topStart: Dp = 0.dp
    var topEnd: Dp = 0.dp
    var bottomStart: Dp = 0.dp
    var bottomEnd: Dp = 0.dp

    if (status == "start") {
        topStart = 15.dp
        topEnd = 15.dp
    } else if (status == "end") {
        bottomStart = 15.dp
        bottomEnd = 15.dp
    }

    Button(
        onClick = { onAction() },
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomStart = bottomStart,
            bottomEnd = bottomEnd
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = ""
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }


            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = "",
            )
        }
    }
}

