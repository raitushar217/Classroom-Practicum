package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subjects(
    @SerializedName("code")
    val SubjectID: String? = null,

    @SerializedName("name")
    val SubjectName: String? = null,

    @SerializedName("description")
    val SubjectDescription: String? = null,

    @SerializedName("lecture_id")
    val LECTURE_ID: String? = null,

    @SerializedName("name")
    val name: String? = null
): Parcelable
