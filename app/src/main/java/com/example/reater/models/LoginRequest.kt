package com.example.reater.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("identification_number")
    var IDNumber: String,
    @SerializedName("password")
    var password: String,

    )
