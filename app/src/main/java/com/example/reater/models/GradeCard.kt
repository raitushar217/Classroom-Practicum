package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AssessmentItem(
    @SerializedName("id")
    var id: String = "",

    @SerializedName("name")
    var name: String = "",

    @SerializedName("max_marks")
    var maxMarks: Double = 100.0,

    @SerializedName("obtained_marks")
    var obtainedMarks: Double = 0.0,

    @SerializedName("weightage")
    var weightage: String = ""
) : Parcelable

@Parcelize
data class GradeCard(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("student_id")
    val studentId: String = "",

    @SerializedName("student_name")
    val studentName: String = "",

    @SerializedName("subject_code")
    val subjectCode: String = "",

    @SerializedName("subject_name")
    val subjectName: String = "",

    @SerializedName("teacher_name")
    val teacherName: String = "",

    @SerializedName("assessments")
    var assessments: List<AssessmentItem> = emptyList(),

    @SerializedName("total_obtained")
    val totalObtained: Double = 0.0,

    @SerializedName("total_max")
    val totalMax: Double = 0.0,

    @SerializedName("percentage")
    val percentage: Double = 0.0,

    @SerializedName("grade")
    val grade: String = "",

    @SerializedName("teacher_remarks")
    var teacherRemarks: String = "",

    @SerializedName("is_published")
    val isPublished: Boolean = false,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) : Parcelable

data class UpdateGradeRequest(
    @SerializedName("assessments")
    val assessments: List<AssessmentItem>,

    @SerializedName("teacher_remarks")
    val teacherRemarks: String
)
