package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Assignment(
    @SerializedName("id")
    var assignmentid: String? = null,

    @SerializedName("section_name")
    var classid: String? = null,

    @SerializedName("subject_name")
    var subjectid: String? = null,

    @SerializedName("due_date")
    var dueDate: String? = null,

    @SerializedName("teacher_name")
    var teacher: String? = null,

    @SerializedName("description")
    var instructions: String? = null,

    @SerializedName("name")
    var assignmentName: String? = null,

    @SerializedName("assignment_pdf")
    var assignmentPdf: String? = null,

    @SerializedName("total_marks")
    var totalMarks: Int? = null,

    // Store the attached file name for local display
    var fileName: String? = null,

    // Store the URI string to open it later
    var fileUri: String? = null
): Parcelable
