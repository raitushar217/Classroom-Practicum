package com.example.reater.models

import com.google.gson.annotations.SerializedName

data class RegisterClassRequest(
    @SerializedName("teaching_assignment_id")
    val teachingAssignmentId: Int? = null,

    @SerializedName("subject_code")
    val subjectCode: String? = null
)
