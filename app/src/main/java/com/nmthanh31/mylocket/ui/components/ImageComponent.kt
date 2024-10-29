package com.nmthanh31.mylocket.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.data.UserViewModel
import com.nmthanh31.mylocket.domain.Post
import com.nmthanh31.mylocket.domain.User
import com.nmthanh31.mylocket.ui.theme.Amber
import com.nmthanh31.mylocket.ui.theme.Background
import com.nmthanh31.mylocket.ui.theme.Charcoal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun ImageComponent(
    post: Post,
    toCamera:() -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val currentUser = Firebase.auth.currentUser

    val userViewModel: UserViewModel = viewModel()
    userViewModel.getUserById(currentUser!!.uid)
    val userSent by userViewModel.user.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(150.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenWidth.dp)
                .clip(shape = RoundedCornerShape(60.dp)),
        ) {
            AsyncImage(
                model = post.photo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Text(
                text = post.content,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
                    .background(Color(0x80000000), shape = RoundedCornerShape(10.dp))
                    .padding(8.dp),
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (if (currentUser.uid == post.uid) "Bạn" else userSent?.name)?.let {
                Text(
                    text = it,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = getTimeDifferenceFromNow(post.time),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.grid), contentDescription = "Turn on flash",  modifier = Modifier.size(30.dp))
            }


            IconButton(
                onClick = toCamera,
                modifier = Modifier
                    .size(50.dp)
                    .border(3.dp, Amber, CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.capture), contentDescription = "Turn on flash", modifier = Modifier.size(35.dp))
            }

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.option), contentDescription = "rotate camera", modifier = Modifier.size(40.dp))
            }
        }

    }
}
fun getTimeDifferenceFromNow(timestamp: Timestamp): String {
    val currentTime = Timestamp.now()

    val pastMillis = timestamp.toDate().time
    val currentMillis = currentTime.toDate().time

    val diffMillis = currentMillis - pastMillis
    val diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

    return when {
        diffMinutes.toInt() == 0 -> "Bây giờ"
        diffMinutes < 60 -> "$diffMinutes phút"
        diffHours < 24 -> "$diffHours giờ"
        diffDays <= 7 -> "$diffDays ngày"
        else -> formatDate(timestamp)
    }
}
fun formatDate(timestamp: Timestamp): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(timestamp.toDate())
}