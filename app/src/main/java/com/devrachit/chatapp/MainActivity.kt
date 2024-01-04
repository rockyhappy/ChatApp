package com.devrachit.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devrachit.chatapp.Constants.Companion.API_SECRET
import com.devrachit.chatapp.screens.SignupScreen
import com.devrachit.chatapp.screens.loginScreen
import com.devrachit.chatapp.ui.theme.ChatappTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String) {
    object SignUpScreen : Screen("sign_up_screen")
    object LoginScreen : Screen("login_screen")
    object ChatListScreen : Screen("chat_list_screen")
    object ProfileScreen : Screen("profile_screen")
    object SingleChatScreen : Screen("single_chat_screen/{chatId}"){
        fun createRoute(chatId: String) = "single_chat_screen/$chatId"
    }
    object SingleStatusScreen : Screen("single_status_screen/{statusId}"){
        fun createRoute(statusId: String) = "single_status_screen/$statusId"
    }
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                  ChatAppNavigation()
                }
            }
        }

    }
    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        val viewModel = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = Screen.SignUpScreen.route){
            composable(Screen.SignUpScreen.route){
                SignupScreen(navController = navController,viewModel = viewModel)
            }
            composable(Screen.LoginScreen.route){
                loginScreen()
            }


        }

        }
    }


