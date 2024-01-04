package com.devrachit.chatapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.devrachit.chatapp.Constants.Companion.USER_NODE
import com.devrachit.chatapp.Data.Event
import com.devrachit.chatapp.Data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore
) : ViewModel() {


    val inProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    init {
        signedIn.value = auth.currentUser != null
        auth.currentUser?.let {
            getUserData(it.uid)
        }
    }
    fun signUp(name: String, email: String, password: String, number: String) {
        inProgress.value = true
        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || number.isEmpty()){
            handleException(customMessage = "Please fill all the fields")
            return
        }
        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if(it.isEmpty){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        signedIn.value = true
                        createOrUpdateProfile(name, number)
                        Log.d("TAG", "createUserWithEmail:success")
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        handleException(task.exception, "Sign up failed")
                    }
                }
            }else{
                handleException(customMessage = "Number already registered")
                inProgress.value = false
            }
        }.addOnFailureListener {
            handleException(it, "Can't Retrieve Data")
        }

    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val user = auth.currentUser?.uid
        val userData = UserData(
            userId = user,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        user?.let {
            inProgress.value = true
            db.collection(USER_NODE).document(user).get().addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")

                if (it.exists()) {
                    //update user profile if already exists
                } else {
                    db.collection(USER_NODE).document(user).set(userData.toMap())
                        .addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot successfully written!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error writing document", e)
                            handleException(e, "Can't Retrieve Data")
                        }
                    inProgress.value = false
                    getUserData(user)
                }


            }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error writing document", e)
                    handleException(e, "Can't Retrieve Data")
                }
        }

    }

    fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null)
                handleException(error, "Can't Retrieve Data")
            if (value != null) {
                var user = value.toObject(UserData::class.java)
                userData.value = user
                inProgress.value = false
            }
        }
    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("TAG", "handleException: ${exception?.localizedMessage ?: customMessage}")
        val errorMessage = exception?.localizedMessage ?: customMessage
        eventMutableState.value = Event(errorMessage)
    }
}