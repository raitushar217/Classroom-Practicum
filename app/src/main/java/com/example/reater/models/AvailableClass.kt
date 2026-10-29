package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AvailableClass(
    @SerializedName("id")
    val id: Int,

    @SerializedName("subject_code")
    val subjectCode: String,

    @SerializedName("subject_name")
    val subjectName: String,

    @SerializedName("teacher_id")
    val teacherId: String,

    @SerializedName("teacher_name")
    val teacherName: String,

    @SerializedName("section_code")
    val sectionCode: String,

    @SerializedName("credits")
    val credits: Int,

    @SerializedName("semester")
    val semester: String,

    @SerializedName("academic_year")
    val academicYear: String,

    @SerializedName("registration_status")
    val registrationStatus: String,

    @SerializedName("is_registered")
    val isRegistered: Boolean
) : Parcelable
