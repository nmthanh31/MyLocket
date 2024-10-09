package com.nmthanh31.mylocket.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.theme.Amber


@Composable
fun CameraComponent() {

    // Lấy context hiện tại của composable, thường cần để truy cập các dịch vụ hoặc tài nguyên hệ thống.
    val context = LocalContext.current

    // Lấy LifecycleOwner được liên kết với composable hiện tại. Điều này được sử dụng để quản lý vòng đời của camera
    val localLifecycleOwner = LocalLifecycleOwner.current


    // Tạo một composable PreviewView, được sử dụng để hiển thị bản xem trước camera.
    // remember là một hàm composable đảm bảo rằng instance PreviewView chỉ được tạo một lần và được sử dụng lại trong các lần recomposition tiếp theo
    val previewView = remember {
        PreviewView(context)
    }

    // Mảng này chứa các quyền cần thiết cho chức năng camera (camera, ghi âm và lưu trữ).
    val CAMERA_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    CAMERA_PERMISSIONS.all {
        // kiểm tra xem tất cả các quyền đã được cấp hay chưa bằng cách sử dụng ContextCompat.checkSelfPermission
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
    //Đây là một hàm composable khởi chạy một activity để yêu cầu các quyền được chỉ định.
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Toast.makeText(context, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    // một biến trạng thái cho biết liệu tất cả các quyền đã được cấp hay chưa.

    val hasAllPermissions by remember {
        derivedStateOf {
            CAMERA_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    // Hàm composable này được sử dụng để khởi chạy một coroutine. Trong trường hợp này, nó kiểm tra xem các quyền đã được cấp chưa và yêu cầu chúng nếu cần
    LaunchedEffect(hasAllPermissions) {
        if (!hasAllPermissions) {
            permissionLauncher.launch(CAMERA_PERMISSIONS)
        }
    }

    // Một biến trạng thái cho biết có đang bật flash không?
    var isFlashing by remember {
        mutableStateOf(false)
    }

    // Một biến trạng thái cho biết có đang chụp không?
    var isCapturing by remember {
        mutableStateOf(false)
    }

    var isFrontOrBack by remember {
        mutableStateOf(true)
    }

    Column{
        
        Spacer(modifier = Modifier.height(150.dp))
        
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .clip(shape = RoundedCornerShape(60.dp)),
        ) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = if (isFrontOrBack == true) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    localLifecycleOwner, cameraSelector, preview
                )
            }, ContextCompat.getMainExecutor(context))
        }

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.flash), contentDescription = "Turn on flash", modifier = Modifier.size(60.dp))
            }


            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .size(110.dp)
                    .border(5.dp, Amber, CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.capture), contentDescription = "Turn on flash", modifier = Modifier.size(90.dp))
            }

            IconButton(
                onClick = {
                    !isFrontOrBack
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.camera), contentDescription = "rotate camera", modifier = Modifier.size(60.dp))
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Lịch sử",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(3.dp))

            Icon(painter = painterResource(id = R.drawable.down), contentDescription = null, tint = Color.White)
        }

    }

}

fun captureImage(isFlashEnabled: Boolean, isFrontOrBack: Boolean){
    val cameraSelector =  if (isFrontOrBack == true) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
    val imageCapture = ImageCapture.Builder().setFlashMode(if (isFlashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF).build()

}
