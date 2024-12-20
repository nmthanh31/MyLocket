package com.nmthanh31.mylocket.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.nfc.Tag
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.nmthanh31.mylocket.data.FriendViewModel
import com.nmthanh31.mylocket.data.FriendViewModelFactory
import com.nmthanh31.mylocket.data.PostViewModel
import com.nmthanh31.mylocket.data.PostViewModelFactory
import com.nmthanh31.mylocket.data.UserViewModel
import com.nmthanh31.mylocket.domain.Friend
import com.nmthanh31.mylocket.domain.FriendStatus
import com.nmthanh31.mylocket.domain.User
import com.nmthanh31.mylocket.ui.bottomsheets.FriendBottomSheet
import com.nmthanh31.mylocket.ui.bottomsheets.ProfileBottomSheet
import com.nmthanh31.mylocket.ui.components.CameraComponent
import com.nmthanh31.mylocket.ui.components.ImageComponent
import com.nmthanh31.mylocket.ui.theme.Amber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    auth: FirebaseAuth
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp -30
    val context = LocalContext.current

    var bottomSheetState by remember {
        mutableStateOf("")
    }

    //bottom sheet
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val sheetActionState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val scope = rememberCoroutineScope()

    val userAuthentication = auth.currentUser!!
    val userViewModel: UserViewModel = viewModel()
    val newUser = User(id = userAuthentication.uid, name = userAuthentication.displayName!!, email = userAuthentication.email!!, photo = userAuthentication.photoUrl.toString())
    userViewModel.addUser(newUser)

    val friendViewModel: FriendViewModel = viewModel(
        factory = FriendViewModelFactory(userAuthentication.uid)
    )
    val friendList by friendViewModel.friends.collectAsState(emptyList())

    val postViewModel: PostViewModel = viewModel(
        factory = PostViewModelFactory(userAuthentication.uid)
    )
    val postList by postViewModel.posts.collectAsState(emptyList())

    //Pager
    val pagerState =  rememberPagerState (initialPage = 0, pageCount = {if (postList.isNotEmpty()) postList.size+1 else 1})
    val scrollScope = rememberCoroutineScope()


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
                else -> ImageComponent(post = postList[page - 1])
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
                    bottomSheetState = "profile"
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
                        bottomSheetState = "friend"
//                        showBottomSheetFriend = true
                        showBottomSheet = true
                    }else{

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
                    text = if(pagerState.currentPage == 0) "${friendList.filter { friend -> friend.status == FriendStatus.FRIENDS.toString() }.size} Bạn bè" else "Tất cả bạn bè",
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

        if (pagerState.currentPage != 0){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = screenHeight.dp),
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
                    onClick = {scrollScope.launch { pagerState.animateScrollToPage(
                        page = 0,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                    ) }},
                    modifier = Modifier
                        .size(60.dp)
                        .border(3.dp, Amber, CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(painter = painterResource(id = R.drawable.capture), contentDescription = "Turn on flash", modifier = Modifier.size(45.dp))
                }

                IconButton(
                    onClick = {
                        bottomSheetState = "action"
//                        showBottomSheetAction = true
                        showBottomSheet = true},
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

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = if (bottomSheetState == "action") Modifier.heightIn(max = 300.dp) else Modifier.fillMaxSize(),
                sheetState = if (bottomSheetState == "action") sheetActionState else sheetState,
                onDismissRequest = { showBottomSheet = false },
                containerColor = MaterialTheme.colorScheme.onBackground,
                dragHandle = { if (bottomSheetState == "action") null else { DragHandle() } }
            ) {
                if (bottomSheetState == "profile"){
                    ProfileBottomSheet(
                        auth,
                        navController,
                        logOut = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showBottomSheet = false
//
//                            }
//                        }

                            scope.launch {
                                userViewModel.removeUserListener()

                                // Điều hướng đến màn hình welcome
                                navController.navigate("welcome") {
                                    popUpTo("home") { inclusive = true }
                                }

                            }


                        }
                    )
                }else if (bottomSheetState == "friend"){
                    FriendBottomSheet(auth)
                }else{
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(text = "Chia sẻ", textAlign = TextAlign.Center, color = Color.White, fontSize = 20.sp)
                        }
                        Button(
                            onClick = {
                                scope.launch {
                                    downloadAndSaveImageToGallery(context, postList[pagerState.currentPage - 1].photo) {  success ->
                                        if (success) {
                                            Log.d("DownloadButton", "Ảnh đã được lưu vào thư viện thành công.")
                                        } else {
                                            Log.e("DownloadButton", "Lưu ảnh thất bại.")
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(text = "Tải xuống", textAlign = TextAlign.Center, color = Color.White, fontSize = 20.sp)
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(text = "Xóa", textAlign = TextAlign.Center, color = Color.Red, fontSize = 20.sp)
                        }
                        Button(
                            onClick = { scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(text = "Hủy", textAlign = TextAlign.Center, color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
suspend fun downloadAndSaveImageToGallery(context: Context, imageUrl: String, onComplete: (Boolean) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            // Mở kết nối tới URL
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream

            // Thiết lập thông tin cho tệp mới
            val fileName = "downloaded_image_${System.currentTimeMillis()}.jpg"
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Locket")
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    outputStream.flush()
                }
                inputStream.close()
                connection.disconnect()
                Log.d("DownloadImage", "Lưu ảnh thành công vào thư viện: $uri")
                onComplete(true)
            } ?: run {
                Log.e("DownloadImage", "Lỗi: Không thể tạo URI")
                onComplete(false)
            }
        } catch (e: Exception) {
            Log.e("DownloadImage", "Lỗi khi lưu ảnh: ${e.message}")
            onComplete(false)
        }
    }
}