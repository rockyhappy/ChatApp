package com.devrachit.chatapp.Data

data class UserData(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    var imageUrl: String? = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
    }
}

data class chatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "chatId" to chatId,
            "user1" to user1,
            "user2" to user2
        )
    }
}

data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    var imageUrl: String? = ""
)

data class Message(
    val sendBy: String? = "",
    val message: String? = "",
    val timeStamp: String? = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "sendBy" to sendBy,
            "message" to message,
            "timeStamp" to timeStamp
        )
    }
}

data class Status(
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp: String? = ""
)
