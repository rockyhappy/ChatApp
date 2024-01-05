package com.devrachit.chatapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.rememberNavController
import com.devrachit.chatapp.R
import com.devrachit.chatapp.Screen
import com.devrachit.chatapp.util.navigateToScreen

enum class BottomNavigationItem(val icon: Int, val navDestination: Screen) {
    HomeScreen(R.drawable.chat, Screen.ChatListScreen),
    ProfileScreen(R.drawable.user, Screen.ProfileScreen),
    StatusScreen(R.drawable.status, Screen.StatusScreen)

}


@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationItem,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 1.dp)
            .background(Color.Black),
        verticalAlignment = Alignment.CenterVertically

    ) {
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateToScreen(
                            navController = navController,
                            route = item.navDestination.route
                        )
                    }

            )
        }
    }
}