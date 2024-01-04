package com.devrachit.chatapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth

): ViewModel(){
    init {

    }
    fun signUp(name: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("TAG", "createUserWithEmail:success")}
                else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}