package com.devrachit.chatapp

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.devrachit.chatapp.Constants.Companion.CHAT_NODE
import com.devrachit.chatapp.Constants.Companion.MESSAGE_NODE
import com.devrachit.chatapp.Constants.Companion.STATUS_NODE
import com.devrachit.chatapp.Constants.Companion.USER_NODE
import com.devrachit.chatapp.Data.ChatUser
import com.devrachit.chatapp.Data.Event
import com.devrachit.chatapp.Data.Message
import com.devrachit.chatapp.Data.Status
import com.devrachit.chatapp.Data.UserData
import com.devrachit.chatapp.Data.chatData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firestore.v1.StructuredQuery
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {


    val inProgress = mutableStateOf(false)
    val inProcessChats = mutableStateOf(false)
    val chats = mutableStateOf<List<chatData>>(listOf())
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signedIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null
    val status = mutableStateOf<List<Status>>(listOf())
    val inProcessStatus = mutableStateOf(false)

    var justUpload = ""

    init {
        signedIn.value = auth.currentUser != null
        auth.currentUser?.let {
            getUserData(it.uid)
        }
    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please fill all the fields")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    signedIn.value = true
                    inProgress.value = false
                    Log.d("TAG", "signInWithEmail:success")
                    auth.currentUser?.let {
                        getUserData(it.uid)
                    }
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    handleException(task.exception, "Sign in failed")
                    inProgress.value = false
                }
            }
        }

    }

    fun signUp(name: String, email: String, password: String, number: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || number.isEmpty()) {
            handleException(customMessage = "Please fill all the fields")
            return
        }
        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
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
            } else {
                handleException(customMessage = "Number already registered")
                inProgress.value = false
            }
        }.addOnFailureListener {
            handleException(it, "Can't Retrieve Data")
        }

    }

    fun uploadProfileImage(uri: Uri, vm: LCViewModel) {
        uploadImage(uri, vm) {
//            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, vm: LCViewModel, onSuccess: (Uri) -> Unit) {
        Log.d("reach", "uploadImage: ")
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        Log.d("TAG", "uploadImage: ${imageRef.path}")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                inProgress.value = false
                vm.userData.value?.imageUrl =
                    "https://firebasestorage.googleapis.com/v0/b/chatapp-3433b.appspot.com/o/images%2F${uuid}?alt=media&token=aa59f1c5-6340-48fe-806c-9e5ff1cce348"
                createOrUpdateProfile(imageUrl = vm.userData.value?.imageUrl)
            }
        }
            .addOnFailureListener {
                handleException(it, "Can't Retrieve Data")
            }

    }

    fun uploadStatusImage(uri: Uri, vm: LCViewModel) {
        inProcessStatus.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        Log.d("TAG", "uploadImage: ${imageRef.path}")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                inProcessStatus.value = false
                vm.justUpload =
                    "https://firebasestorage.googleapis.com/v0/b/chatapp-3433b.appspot.com/o/images%2F${uuid}?alt=media&token=aa59f1c5-6340-48fe-806c-9e5ff1cce348"
                createStatus(vm.justUpload)
            }
        }
            .addOnFailureListener {
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
                    db.collection(USER_NODE).document(user).update(userData.toMap())
                        .addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot successfully written!")
                            inProgress.value = false
                        }
                        .addOnFailureListener { e ->
                            Log.w("TAG", "Error writing document", e)
                            handleException(e, "Can't Retrieve Data")
                            inProgress.value = false
                        }
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
                populateStatus()
                Log.d("error",status.value.toString())
                populateChats()
            }
        }
    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("TAG", "handleException: ${exception?.localizedMessage ?: customMessage}")
        val errorMessage = exception?.localizedMessage ?: customMessage
        eventMutableState.value = Event(errorMessage)
    }

    fun logout() {
        auth.signOut()
        userData.value = null
        signedIn.value = false
        depopulateMessages()
        currentChatMessageListener = null
        eventMutableState.value = Event("Logged out successfully")
    }

    fun addChat(number: String) {

        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number must contain Digits only")
            return
        } else {
            Log.d("TAG", "addChat: $number")
            db.collection(CHAT_NODE).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", userData.value?.number),
                        Filter.equalTo("user2.number", number)
                    ),
                    Filter.and(
                        Filter.equalTo("user2.number", userData.value?.number),
                        Filter.equalTo("user1.number", number)
                    )
                )
            ).get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        db.collection(USER_NODE).whereEqualTo("number", number).get()
                            .addOnSuccessListener {
                                if (it.isEmpty) {
                                    handleException(customMessage = "User not found")
                                } else {
                                    val chatPartner = it.toObjects<UserData>()[0]
                                    val id = db.collection(CHAT_NODE).document().id
                                    val chat = chatData(
                                        chatId = id,
                                        user1 = ChatUser(
                                            userId = userData.value?.userId,
                                            name = userData.value?.name,
                                            number = userData.value?.number,
                                            imageUrl = userData.value?.imageUrl
                                        ),
                                        user2 = ChatUser(
                                            userId = chatPartner.userId,
                                            name = chatPartner.name,
                                            number = chatPartner.number,
                                            imageUrl = chatPartner.imageUrl
                                        )
                                    )
                                    db.collection(CHAT_NODE).document(id).set(chat.toMap())
                                        .addOnSuccessListener {
                                            Log.d("TAG", "DocumentSnapshot successfully written!")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w("TAG", "Error writing document", e)
                                            handleException(e, "Can't Retrieve Data")
                                        }
                                }
                            }
                    } else {
                        handleException(customMessage = "Chat already exists")

                    }
                }

        }
    }

    fun populateChats() {
        inProcessChats.value = true
        db.collection(CHAT_NODE)
            .where(
                Filter.or(
                    Filter.equalTo("user1.userId", userData.value?.userId),
                    Filter.equalTo("user2.userId", userData.value?.userId)
                )
            )
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                }
                if (value != null) {
                    chats.value = value.documents.mapNotNull {
                        it.toObject<chatData>()
                    }
                    inProcessChats.value = false
                }
            }

    }

    fun onSendReply(message: String, chatId: String) {
        val id = db.collection(CHAT_NODE).document().id
        val messageData = Message(
            sendBy = userData.value?.userId,
            message = message,
            timeStamp = System.currentTimeMillis().toString()
        )

        db.collection(CHAT_NODE).document(chatId).collection(MESSAGE_NODE)
            .document()
            .set(messageData.toMap()).addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error writing document", e)
                handleException(e, "Can't Retrieve Data")
            }
    }

    fun populateMessages(chatId: String) {
        inProgressChatMessages.value = true
        currentChatMessageListener =
            db.collection(CHAT_NODE).document(chatId).collection(MESSAGE_NODE)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        handleException(error)
                    }
                    if (value != null) {
                        chatMessages.value = value.documents.mapNotNull {
                            it.toObject<Message>()
                        }.sortedBy {
                            it.timeStamp
                        }
                        inProgressChatMessages.value = false
                    }
                }
    }

    fun depopulateMessages() {
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun uploadStatus(uri: Uri, vm: LCViewModel) {
        uploadStatusImage(uri, vm)
    }

    fun createStatus(imageUrl: String) {
        val newStatus = Status(
            ChatUser(
                userId = userData.value?.userId,
                name = userData.value?.name,
                number = userData.value?.number,
                imageUrl = userData.value?.imageUrl
            ),
            imageUrl,
            System.currentTimeMillis().toString()
        )
        db.collection(STATUS_NODE).document().set(newStatus).addOnSuccessListener {}

    }

    fun populateStatus() {
//        val timeDelta = 24 * 60 * 60 * 1000
//        val currentTime = System.currentTimeMillis()
//        val timeLimit = currentTime - timeDelta
        inProcessStatus.value = true
//        db.collection(CHAT_NODE).where(
//            Filter.or(
//                Filter.equalTo("user1.userId", userData.value?.userId),
//                Filter.equalTo("user2.userId", userData.value?.userId)
//            )
//        ).addSnapshotListener { value, error ->
//            if (error != null) {
//                handleException(error)
//            }
//            if (value != null) {
//                val currentConnections = arrayListOf(userData.value?.userId)
//                val chats = value.toObjects<chatData>()
//                chats.forEach {
//                    if (it.user1.userId == userData.value?.userId) {
//                        currentConnections.add(it.user2.userId)
//                    } else {
//                        currentConnections.add(it.user1.userId)
//                    }
//                }
                db.collection(STATUS_NODE)
//                    .whereGreaterThan("timestamp", timeLimit)
//                    .whereIn("user.userId", currentConnections)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            handleException(error)
                        }
                        if (value != null) {
                            status.value = value.toObjects()
                            Log.d("error", status.value.toString())
                            inProcessStatus.value = false
                        }
                    }

//            inProcessStatus.value = false
//        }
    }
}
