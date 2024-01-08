package com.devrachit.chatapp.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devrachit.chatapp.LCViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.devrachit.chatapp.Screen
import com.devrachit.chatapp.util.CommonDivider
import com.devrachit.chatapp.util.CommonRow
import com.devrachit.chatapp.util.TitleText
import com.devrachit.chatapp.util.customProgressBar
import com.devrachit.chatapp.util.navigateToScreen
import androidx.compose.foundation.lazy.items

@Composable
fun StatusScreen(vm: LCViewModel, navController: NavController) {

    val inProgress = vm.inProcessStatus.value
    if (inProgress) {
        customProgressBar()
    } else {
        val status = vm.status.value
        val userData = vm.userData.value
        val myStatuses = status.filter { it.user.userId == userData?.userId }
        val otherStatuses = status.filter { it.user.userId != userData?.userId }
        val launcherForStatus= rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent() ){
            uri->
            uri?.let{
                vm.uploadStatus(it,vm)
            }
        }
        Scaffold(
            bottomBar = {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.StatusScreen,
                    navController = navController
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FAB {
                    launcherForStatus.launch("image/*")
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TitleText(text = "Status")
                    if (status.isEmpty()) {
                        Column(
                            modifier= Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(text = "No Statuses Available")
                        }
                    } else {
                        if(myStatuses.isNotEmpty()){
                            CommonRow(imageUrl = myStatuses[0].user.imageUrl, name =myStatuses[0].user.name ) {
                                    navigateToScreen(
                                        navController = navController,Screen.SingleStatusScreen.createRoute(myStatuses[0].user.userId!!)
                                    )
                            }
                            CommonDivider()
                            val uniqueUser=otherStatuses.map{it.user}.toSet().toList()
                            LazyColumn(modifier =Modifier.weight(1f) )
                            {
                                items(uniqueUser){user->
                                    CommonRow(imageUrl = user.imageUrl, name = user.name) {
                                        navigateToScreen(navController = navController,Screen.SingleStatusScreen.createRoute(user.userId!!))
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            }
        )

    }


}

@Composable
fun FAB(
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = Color.Blue,
        shape = CircleShape,
        contentColor = Color.White,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add Chat",
            tint = Color.White
        )
    }
}