package com.devrachit.chatapp.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel
import com.devrachit.chatapp.Screen
import com.devrachit.chatapp.util.CommonDivider
import com.devrachit.chatapp.util.CommonImage
import com.devrachit.chatapp.util.customProgressBar
import com.devrachit.chatapp.util.navigateToScreen


@Composable
fun ProfileScreen(vm: LCViewModel, navController: NavController) {
    val inProgress = vm.inProgress.value
    if (inProgress) {
        customProgressBar()
    } else {
        val userData= vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "") }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }

        Scaffold (
            bottomBar = {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.ProfileScreen,
                    navController = navController
                )
            },
            content={paddingValues ->
                Log.d("error",paddingValues.toString())
            }
        )
        Column {
            ProfileContent(
                modifier = Modifier
//                    .weight(0.9f)
                    .wrapContentSize(Alignment.TopCenter)
                    .padding(8.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                vm = vm,
                name = name,
                number = number,
                onNameChange = {name=it},
                onNumberChange = {number=it},
                onBack = {
                    navController.popBackStack()
                },
                onSave = {
                    vm.createOrUpdateProfile(name,number)
                },
                onLogout = {
                    vm.logout()
                    navigateToScreen(navController = navController, route = Screen.LoginScreen.route)
                }
            )
        }

    }
}

@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogout: () -> Unit
) {
    Column (modifier= modifier){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", modifier = Modifier.clickable {
                onSave.invoke()
            })
        }
        CommonDivider()
        ProfileImage(imageUrl = vm.userData.value?.imageUrl, vm = vm)
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp,end=20.dp,top=0.dp,bottom = 10.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,

                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp,end=20.dp,top=10.dp,bottom = 40.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number,
                onValueChange = onNumberChange,

                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.Gray,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                )
            )
        }
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable {
                onLogout.invoke()
            })
        }
    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                vm.uploadProfileImage(it,vm)
            }
        }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min))
    {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            )
            {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Image", modifier = Modifier.padding(8.dp))
        }
        if (vm.inProgress.value) {
            customProgressBar()
        }

    }
}