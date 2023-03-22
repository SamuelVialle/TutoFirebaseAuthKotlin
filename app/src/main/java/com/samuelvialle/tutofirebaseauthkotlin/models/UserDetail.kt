package com.samuelvialle.tutofirebaseauthkotlin.models

class UserDetail(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val profileCompleted: Int = 0
)