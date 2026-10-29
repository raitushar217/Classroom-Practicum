package com.example.reater.models

import com.google.gson.annotations.SerializedName

data class TeacherLoginResponse(
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val user: UserDto,

    @SerializedName("code")
    var code: Int? = null,

    @SerializedName("message")
    var message: String? = null
)
