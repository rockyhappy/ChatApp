package com.devrachit.chatapp.util

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.Screen


fun navigateToScreen(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route) {
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

@Composable
fun CheckSignedIn(navController: NavController, viewModel: LCViewModel) {
    val alreadySignedIn = remember { mutableStateOf(false) }
    val signedIn = viewModel.signedIn.value
    if (signedIn && !alreadySignedIn.value) {
        alreadySignedIn.value = true
        navController.navigate(Screen.ChatListScreen.route)
        {
            popUpTo(0)
        }
    }
}

@Composable
fun CommonDivider() {
    Divider(
        color = Color.White,
        thickness = 4.dp,
        modifier = Modifier
            .alpha(0.4f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale: ContentScale = ContentScale.Crop
) {
    Log.d("CommonImage", "CommonImage: $data")
    val painter = rememberAsyncImagePainter(model = data)
    Box(
        modifier = modifier.background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )
    }

}
@Composable
fun TitleText(text: String) {
    Text(text=text,fontSize = 30.sp,fontWeight = FontWeight.Bold,modifier = Modifier.padding(8.dp))
}