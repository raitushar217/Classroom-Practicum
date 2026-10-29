package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDto(
    @SerializedName("identification_number")
    val identificationNumber: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("name")
    val name: String? = null
) : Parcelable
