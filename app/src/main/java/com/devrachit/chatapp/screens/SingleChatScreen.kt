package com.devrachit.chatapp.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devrachit.chatapp.Data.Message
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.util.CommonDivider
import com.devrachit.chatapp.util.CommonImage

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {
    Log.d("Single Chat Screen", "SingleChatScreen: $chatId")

    var reply by rememberSaveable { mutableStateOf("") }
    val onSendReply = {
        vm.onSendReply(reply, chatId)
        reply = ""
    }
    val myUser = vm.userData.value
    val currentChat = vm.chats.value.first { it.chatId == chatId }
    val otherUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    LaunchedEffect(key1 = Unit) {
        vm.populateMessages(chatId)
    }
    BackHandler {
        vm.depopulateMessages()
        navController.popBackStack()
    }
    Column {
        chatHeader(name = otherUser.name ?: "", imageUrl = otherUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.depopulateMessages()
        }
        messageBox(
            modifier = Modifier.weight(1f),
            chatMessages = vm.chatMessages.value,
            currentUserId = myUser?.userId ?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
    }


}

@Composable
fun chatHeader(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
            .clickable {
                onBackClicked()
            }
            .padding(8.dp))
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )

    }

}


@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = reply, onValueChange = onReplyChange, maxLines = 4, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            )
            Button(modifier = Modifier.width(100.dp), onClick = onSendReply) {
                Text(text = "Send")
            }
        }

    }

}

@Composable
fun messageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xFF3F51B5) else Color(0xFF607D8B)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}