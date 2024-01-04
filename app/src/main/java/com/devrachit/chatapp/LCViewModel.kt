package com.devrachit.chatapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.devrachit.chatapp.Data.Event
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth

): ViewModel(){
    init {

    }
    val inProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signedIn = mutableStateOf(false)
    fun signUp(name: String, email: String, password: String){
        inProgress.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    signedIn.value = true
                    //createOrUpdateUser(name, number)
                    Log.d("TAG", "createUserWithEmail:success")}
                else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    handleException(task.exception, "Sign up failed")
                }
            }
    }
    fun handleException(exception: Exception?=null, customMessage: String=""){
        Log.e("TAG", "handleException: ${exception?.localizedMessage ?: customMessage}")
        val errorMessage= exception?.localizedMessage ?: customMessage
            eventMutableState.value = Event(errorMessage)
    }
}