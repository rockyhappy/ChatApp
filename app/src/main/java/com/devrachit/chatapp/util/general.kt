package com.devrachit.chatapp.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

fun navigateToScreen(navController: NavController, route: String) {
    navController.navigate(route){
        popUpTo(route){
            inclusive = true
        }
        launchSingleTop = true
    }

}
@Composable
 fun customProgressBar() {
     Row(
         modifier = Modifier
             .alpha(0.5f)
             .fillMaxSize()
             .background(Color.Black)
             .wrapContentWidth(Alignment.CenterHorizontally)
             .wrapContentHeight(Alignment.CenterVertically),
         horizontalArrangement = Arrangement.Center,
         verticalAlignment = Alignment.CenterVertically
     ) {
         CircularProgressIndicator(
             color = Color.White,
             modifier = Modifier
                 .size(50.dp)
         )
     }
 }