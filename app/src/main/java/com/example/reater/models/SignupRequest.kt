package com.example.reater.models

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("identification_number")
    var Enrollment: String,
    
    @SerializedName("password")
    var password: String,

    @SerializedName("first_name")
    var first_name: String,

    @SerializedName("last_name")
    var last_name: String,

    @SerializedName("batch")
    var batch: String,

    @SerializedName("section_code")
    var section_code: String
)
