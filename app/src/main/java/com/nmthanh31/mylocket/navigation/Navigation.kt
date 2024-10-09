package com.nmthanh31.mylocket.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.nmthanh31.mylocket.ui.screens.ChatScreen
import com.nmthanh31.mylocket.ui.screens.ChooseNameScreen
import com.nmthanh31.mylocket.ui.screens.ChoosePasswordScreen
import com.nmthanh31.mylocket.ui.screens.ChooseUsernameScreen
import com.nmthanh31.mylocket.ui.screens.EnterPasswordScreen
import com.nmthanh31.mylocket.ui.screens.HomeScreen
import com.nmthanh31.mylocket.ui.screens.ProfileScreen
import com.nmthanh31.mylocket.ui.screens.RegisterAndLoginScreen
import com.nmthanh31.mylocket.ui.screens.WelcomeScreen

@SuppressLint("RestrictedApi")
@Composable
fun MyLocketNavHost() {
    val navController = rememberNavController()

    var auth: FirebaseAuth = Firebase.auth

    val currentUser = auth.currentUser

    var startDestination = "welcome"

    if (currentUser != null) {
        startDestination = "home"
    }



    NavHost(navController = navController, startDestination = startDestination) {
        composable("welcome") {
            WelcomeScreen(
                onNavigateToRegister = {
                    navController.navigate(
                        route = "registerAndLogin/register"
                    )
                },
                onNavigateToLogin = {
                    navController.navigate(
                        route = "registerAndLogin/login"
                    )
                }
            )
        }
        composable("registerAndLogin/{registerOrLogin}") { backStackEntry ->
            val registerOrLogin = backStackEntry.arguments?.getString("registerOrLogin")
            RegisterAndLoginScreen(
                navController = navController,
                registerOrLogin = registerOrLogin
            )
        }
        composable("chooseName") {
            ChooseNameScreen(
                navController = navController,
                auth = auth
            )
        }
        composable("chooseUserName/{email}/{password}") {

            ChooseUsernameScreen(
                navController = navController,
                auth = auth
            )
        }
        composable("choosePassword/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            ChoosePasswordScreen(
                auth = auth,
                navController = navController,
                email = email
            )
        }
        composable("enterPassword/{email}") { navBackStackEntry ->
            val email = navBackStackEntry.arguments?.getString("email")
            EnterPasswordScreen(navController = navController, auth = auth , email = email)
        }
        composable("home") { HomeScreen(navController = navController, auth = auth) }
        composable("chat") { ChatScreen(navController = navController) }
    }
}