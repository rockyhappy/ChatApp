package com.devrachit.chatapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel

@Composable
fun StatusScreen(vm: LCViewModel, navController: NavController) {
    BottomNavigationMenu(
        selectedItem = BottomNavigationItem.StatusScreen,
        navController = navController
    )

}