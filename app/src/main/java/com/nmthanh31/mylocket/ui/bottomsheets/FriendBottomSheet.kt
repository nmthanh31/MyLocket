package com.nmthanh31.mylocket.ui.bottomsheets

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.domain.Friend
import com.nmthanh31.mylocket.domain.FriendStatus
import com.nmthanh31.mylocket.domain.User
import com.nmthanh31.mylocket.ui.theme.Amber
import com.nmthanh31.mylocket.ui.theme.Grey

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendBottomSheet(
    auth: FirebaseAuth,
    firebase: Firebase
) {

    var searchInput by remember { mutableStateOf("") }

    val userList = mutableListOf<User>()


    val friendList = mutableListOf<Friend>()

    var filteredUser by remember {
        mutableStateOf(mutableListOf<User>())
    }

    val currentUser = auth.currentUser!!

    val db = Firebase.firestore

    val friendCollectionRef = db.collection("users").document(currentUser.uid).collection("friends")

    val context = LocalContext.current

    friendCollectionRef.get()
        .addOnSuccessListener { querySnapshot->

            if (!querySnapshot.isEmpty){
                val list = querySnapshot.documents
                for (document in list){
                    val uid = document.getString("uid")
                    val friendName = document.getString("name")
                    val friendEmail = document.getString("email")
                    val friendPhoto = document.getString("photo")
                    val friendStatus = document.getString("status")

//                    val friend = document.toObject(Friend::class.java)
                    val friend = Friend(id = uid!!, name = friendName!!, email = friendEmail!!, photo = friendPhoto, status = friendStatus!!)
                    friendList.add(friend)
                }
            }
        }
        .addOnFailureListener { Toast.makeText(context, "ko lay duuoc ne", Toast.LENGTH_SHORT).show() }

    db.collection("users").get()
        .addOnSuccessListener { result->
            if (!result.isEmpty){
                for (document in result.documents){
                    val userId = document.getString("id")

                    if (userId != currentUser.uid && userId !in friendList.map { it.id }) {
                        // Lấy các thông tin khác của người dùng
                        val userName = document.getString("name")
                        val userEmail = document.getString("email")
                        val userPhoto = document.getString("photo")
//
//                        // Xử lý logic ở đây, ví dụ thêm người dùng vào danh sách
                        val user = User(userId!!, userName!!, userEmail!!, userPhoto)
//                        val user = document.toObject(User::class.java)
                        Log.e("Firebase Data", userId+" "+userEmail)
                        userList.add(user)
                    }
                }
            }
            Log.e("Data", userList.toString())
        }
        .addOnFailureListener { Toast.makeText(context, "ko lay duuoc ne", Toast.LENGTH_SHORT).show() }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bạn bè của bạn",
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
        )
        Text(
            text = "${friendList.size} / 20 người bạn đã được bổ sung",
            style = TextStyle(
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )

        TextField(
            value = searchInput,
            onValueChange = {input->
                run {
                    searchInput = input
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray
            ),
            placeholder = {
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription ="" )

                    Text(
                        text = "Thêm một người bạn mới",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        ),
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .border(
                    width = 2.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    filteredUser = userList.filter { user ->
                        user.email.contains(searchInput, ignoreCase = true)}.toMutableList()
                }
            )
        )

        if (filteredUser.isNotEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_friend_add), contentDescription = "", tint = Color.Gray)

                Text(
                    text = "Tìm kiếm bạn bè",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            filteredUser.map { user-> CustomLine(user = user) }


        }

        if (!friendList.map { it.status== FriendStatus.PENDING.toString()}.isEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_friend_add), contentDescription = "", tint = Color.Gray)

                Text(
                    text = "Yêu cầu kết bạn",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }


        friendList.map {item ->
            if (item.status == FriendStatus.PENDING.toString()){
                CustomLine(friend = item)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.friend), contentDescription = "", tint = Color.Gray)

            Text(
                text = "Bạn bè của bạn",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        if (friendList.map { it.status== FriendStatus.FRIENDS.toString()}.isEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hãy thêm bạn bè để chia sẻ khoảnh khắc",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }

//        friendList.map {item ->
//            if (item.status == FriendStatus.FRIENDS.toString()){
//                CustomLine(friend = item)
//            }
//        }
        userList.map { item -> CustomLine(user = item) }
    }
}



@Composable
fun CustomLine(
    friend: Friend
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (friend.photo == null) painterResource(id = R.drawable.user) else painterResource(id = R.drawable.img),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = Amber, shape = CircleShape)
            )

            Text(
                text = friend.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        if (friend.status == FriendStatus.FRIENDS.toString()){
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Grey,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "")
            }
        }else{
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Grey,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_check), contentDescription = "")
            }
        }
    }


}

@Composable
fun CustomLine(user: User) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = if (user.photo == null) painterResource(id = R.drawable.user) else painterResource(id = R.drawable.img),
                contentDescription = "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = Amber, shape = CircleShape)
            )

            Text(
                text = user.name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")

                Text(text = "Thêm")
            }
        }
    }

}
