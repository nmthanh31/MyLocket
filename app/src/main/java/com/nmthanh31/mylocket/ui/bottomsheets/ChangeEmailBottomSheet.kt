package com.nmthanh31.mylocket.ui.bottomsheets

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.nmthanh31.mylocket.R
import com.nmthanh31.mylocket.ui.screens.SignIn
import com.nmthanh31.mylocket.ui.theme.Amber
import com.nmthanh31.mylocket.ui.theme.Background
import com.nmthanh31.mylocket.ui.theme.Charcoal

@Suppress("DEPRECATION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmailBottomSheet(
    auth: FirebaseAuth,
    email: String?
) {
    var stateComposable by remember {
        mutableStateOf(false)
    }

    var password by remember {
        mutableStateOf("")
    }

    var emailUpdate by remember {
        mutableStateOf(email)
    }

    val context = LocalContext.current

    val user = auth.currentUser!!





    if (stateComposable == false){
        ConfirmPassword(
            onContinue = {
                val credential = email?.let { EmailAuthProvider.getCredential(it, password) }
                if (credential != null) {
                    user.reauthenticate(credential)
                        .addOnCompleteListener { task ->
                            run {
                                if (task.isSuccessful) {
                                    stateComposable = true
                                } else {
                                    Toast.makeText(context, "Sai mật khẩu", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }

            },
            password = password,
            onChangeValue = {input->password=input}
        )
    }else{
        emailUpdate?.let {
            ChangeEmail(
                onBack = {stateComposable = !stateComposable},
                emailUpdate= it,
                onChangeValueEmail = { input->emailUpdate=input},
                onChangeEmail = {
                    if (user.email != emailUpdate) {
                        val credential = EmailAuthProvider.getCredential(user.email!!,password) // Mật khẩu hiện tại của người dùng

                        user.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                            if (reauthTask.isSuccessful) {
                                // Sau khi xác thực lại, tiếp tục cập nhật email
                                user.updateEmail(emailUpdate!!)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Email đã được cập nhật", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Lỗi khi cập nhật email", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Xác thực lại thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "The email is the same as the current one", Toast.LENGTH_SHORT).show()
                    }

                }
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeEmail(
    onBack: ()->Unit,
    emailUpdate: String,
    onChangeValueEmail:(String) -> Unit,
    onChangeEmail: () ->Unit
) {



    val isEmailValid = remember(emailUpdate) { Patterns.EMAIL_ADDRESS.matcher(emailUpdate).matches() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { onBack() },
            modifier = Modifier
                .padding(start = 20.dp, top = 30.dp)
                .clip(shape = CircleShape)
                .size(50.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White,
                containerColor = Charcoal
            )
        ) {
            Icon(painter = painterResource(id = R.drawable.back), contentDescription = null)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Thay đổi email của bạn",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = emailUpdate,
                onValueChange = onChangeValueEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
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
                    Text(text = "Địa chỉ email")
                },
                singleLine = true,
            )

        }

        Button(
            onClick = onChangeEmail,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber,
                contentColor = Color.Black,
                disabledContainerColor = Charcoal,
                disabledContentColor = Color(0xFF4E4E50)
            ),
            enabled = isEmailValid,

            ) {
            Row {
                Text(
                    text = "Lưu",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPassword(
    onContinue: ()->Unit,
    password: String,
    onChangeValue: (String)->Unit
) {


    val isPassword = remember(password) {
        !password.isEmpty()
    }

    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground)
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Điền mật khẩu của bạn",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = password,
                onValueChange = onChangeValue,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
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
                    Text(text = "Mật khẩu")
                },
                trailingIcon = {

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (passwordVisible) painterResource(id = R.drawable.ic_visibility_off) else painterResource(
                                id = R.drawable.ic_visibility
                            ), contentDescription = ""
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            )
        }

        Button(
            onClick = {
                onContinue()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber,
                contentColor = Color.Black,
                disabledContainerColor = Charcoal,
                disabledContentColor = Color(0xFF4E4E50)
            ),
            enabled = isPassword,

            ) {
            Row {
                Text(
                    text = "Tiếp tục",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null,
                    )
            }
        }

    }
}