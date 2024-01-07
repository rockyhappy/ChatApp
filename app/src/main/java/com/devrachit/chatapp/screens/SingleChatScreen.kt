package com.devrachit.chatapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.util.CommonDivider

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {
    Log.d("Single Chat Screen", "SingleChatScreen: $chatId")
    Text(text = chatId, color = Color.White)
    var reply by rememberSaveable{ mutableStateOf("") }
    ReplyBox(reply = reply, onReplyChange ={reply=it} ) {
        
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
                TextField(value = reply, onValueChange =onReplyChange, maxLines = 4 )
                Button(onClick = onSendReply) {
                    Text(text ="Send")
                }
        }

    }
}