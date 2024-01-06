package com.devrachit.chatapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.R
import com.devrachit.chatapp.util.TitleText
import com.devrachit.chatapp.util.customProgressBar


@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel) {

    val inProgress = vm.inProcessChats.value
    if (inProgress) {
        customProgressBar()
    } else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember { mutableStateOf(false) }
        val onFabClick: () -> Unit = {
            showDialog.value = true
        }
        val onDismiss: () -> Unit = {
            showDialog.value = false
        }
        val onAddChat: (String) -> Unit = {
            vm.addChat(it)
            showDialog.value = false
        }
        Scaffold(
            bottomBar = {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.ProfileScreen,
                    navController = navController
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = onFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            },
            content = { paddingValues ->
                Log.d("error", paddingValues.toString())
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TitleText(text = "Chats")
                    if (chats.isEmpty()) {
                        Column(modifier=Modifier.fillMaxSize().weight(1f),horizontalAlignment = Alignment.CenterHorizontally,verticalArrangement = Arrangement.Center) {
                            Text(text = "No Chats Yet")
                            Text(text = "Click on the + button to add a chat")
                        }

                    }
                }
                }

            )

        }

    }

    @Composable
    fun FAB(
        showDialog: Boolean,
        onFabClick: () -> Unit,
        onDismiss: () -> Unit,
        onAddChat: (String) -> Unit
    ) {
        val addChatNumber = remember { mutableStateOf("") }
        if (showDialog) {
            AlertDialog(
                title = {
                    Text(text = "Add Chat Member")
                },
                text = {
                    OutlinedTextField(
                        value = addChatNumber.value,
                        onValueChange = { addChatNumber.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                },
                onDismissRequest = {
                    onDismiss.invoke()
                    addChatNumber.value = ""
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                        onAddChat(addChatNumber.value)
                        }) {
                        Text(text = "Add")
                    }
                }
            )
        }
        FloatingActionButton(
            onClick = {
                onFabClick.invoke()
            },
            containerColor = Color.Gray,
            shape = CircleShape,
            modifier = Modifier
                .padding(40.dp)

        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                tint = Color.White,
                contentDescription = "Add Chat"
            )
        }

    }