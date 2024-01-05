package com.devrachit.chatapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.R
import com.devrachit.chatapp.Screen
import com.devrachit.chatapp.util.CheckSignedIn
import com.devrachit.chatapp.util.navigateToScreen

@Composable
fun loginScreen(navController : NavController, viewModel :LCViewModel) {
    CheckSignedIn(navController = navController, viewModel =viewModel )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .background(Color.Black)
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }
            val focus = LocalFocusManager.current

            Image(
                painter =
                painterResource(id = R.drawable.chat),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign In",
                fontFamily = FontFamily.Serif,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                label = {
                    Text(text = "Email")
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 8.dp)
                    .fillMaxSize()
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                label = {
                    Text(text = "Password")
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 8.dp)
                    .fillMaxSize()
            )
            Button(
                onClick = {
                    viewModel.Login(emailState.value.text,passwordState.value.text)
                }, modifier = Modifier
                    .padding(8.dp)
                    .width(170.dp)
            ) {
                Text(text = "Login")

            }
            Text(text = "New User ? SignUp", color = Color.White, modifier = Modifier
                .padding(8.dp)
                .clickable {
                    navigateToScreen(
                        navController = navController,
                        route = Screen.SignUpScreen.route
                    )
                })

        }
        if(viewModel.inProgress.value){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable {
                        viewModel.inProgress.value = false
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight()
                        .background(Color.Black)
                        .verticalScroll(
                            rememberScrollState()
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Logging in ...", color = Color.White, modifier = Modifier.padding(8.dp))
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
            }
        }
    }
}