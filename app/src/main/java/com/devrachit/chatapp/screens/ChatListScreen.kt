package com.devrachit.chatapp.screens

import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel

@Composable
fun ChatListScreen(navController: NavController,vm : LCViewModel) {

    Text(text = "ChatListScreen", color = Color.Black, modifier = Modifier.background(Color.White))
    BottomNavigationMenu(selectedItem = BottomNavigationItem.HomeScreen, navController = navController)
}