package com.devrachit.chatapp.util

import androidx.navigation.NavController

fun navigateToScreen(navController: NavController, route: String) {
    navController.navigate(route){
        popUpTo(route){
            inclusive = true
        }
        launchSingleTop = true
    }

}