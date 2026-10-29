package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssignmentSubmission(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("student")
    val studentId: String = "",

    @SerializedName("student_name")
    val studentName: String = "",

    @SerializedName("marks")
    var marks: Int? = null,

    @SerializedName("feedback")
    var feedback: String = "",

    @SerializedName("status")
    var status: String = "submitted",

    @SerializedName("submission_pdf")
    val submissionPdf: String = "",

    @SerializedName("submitted_at")
    val submittedAt: String = "",

    @SerializedName("assignment_id")
    val assignmentId: String = "",

    @SerializedName("assignment_name")
    val assignmentName: String = "",

    @SerializedName("total_marks")
    val totalMarks: Int? = 100
) : Parcelable

data class GradeSubmissionRequest(
    @SerializedName("marks")
    val marks: Int,

    @SerializedName("feedback")
    val feedback: String
)
