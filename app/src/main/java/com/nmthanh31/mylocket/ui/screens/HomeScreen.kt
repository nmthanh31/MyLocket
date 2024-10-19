package com.nmthanh31.mylocket.ui.screens

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.Charcoal
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nmthanh31.mylocket.ui.bottomsheets.FriendBottomSheet
import com.nmthanh31.mylocket.ui.bottomsheets.ProfileBottomSheet
import com.nmthanh31.mylocket.ui.components.CameraComponent
import com.nmthanh31.mylocket.ui.components.ImageComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    auth: FirebaseAuth
) {
    //Pager
    val pagerState =  rememberPagerState (initialPage = 0, pageCount = {10})

    //bottom sheet
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showBottomSheetFriend by remember {
        mutableStateOf(false)
    }

    val sheetFriendState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val context = LocalContext.current

    //Firestore
    val firebase = Firebase
    val db = firebase.firestore
    val userAuthentication = auth.currentUser!!
    val uid = userAuthentication.uid
    val userDocRef = db.collection("users").document(uid)

    userDocRef.get()
        .addOnSuccessListener { document ->
            if (!document.exists()) {
                // Tài liệu với uid chưa tồn tại, có thể thêm người dùng mới
                val newUser = hashMapOf(
                    "id" to uid,
                    "name" to userAuthentication.displayName,
                    "email" to userAuthentication.email,
                    "photo" to userAuthentication.photoUrl?.toString()
                )
                userDocRef.set(newUser)
                    .addOnSuccessListener {
                        Toast.makeText(context, "New user added", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to add user: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Failed to check user: ${e.message}", Toast.LENGTH_SHORT).show()
        }


    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) {page ->
            when (page){
                0 -> CameraComponent(navController = navController)
                else -> ImageComponent()
            }

        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 75.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Charcoal,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            Button(
                onClick = {
                    if (pagerState.currentPage == 0){
                        showBottomSheetFriend = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Charcoal,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.friend),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = if(pagerState.currentPage == 0) "1 Bạn bè" else "Tất cả bạn bè",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = { navController.navigate("chat") },
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Charcoal,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                containerColor = MaterialTheme.colorScheme.onBackground

            ) {
                ProfileBottomSheet(
                    auth,
                    navController,
                    onClosed = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState)
            }
        }
        if (showBottomSheetFriend) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetFriendState,
                onDismissRequest = { showBottomSheetFriend = false },
                containerColor = MaterialTheme.colorScheme.onBackground

            ) {
                FriendBottomSheet(auth, firebase)
            }
        }
    }
}


