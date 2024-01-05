package com.devrachit.chatapp.Data

data class UserData(
    val userId: String?="",
    val name: String?="",
    val number: String?="",
    var imageUrl: String?=""
)
{
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
    }
}