package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceSessionLog(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("slot")
    val slot: String = "",
    @SerializedName("status")
    val status: String = "present"
) : Parcelable

@Parcelize
data class StudentAttendanceSummary(
    @SerializedName("subject_code")
    val subjectCode: String = "",
    @SerializedName("subject_name")
    val subjectName: String = "",
    @SerializedName("teacher_name")
    val teacherName: String = "",
    @SerializedName("total_lectures")
    val totalLectures: Int = 0,
    @SerializedName("attended_lectures")
    val attendedLectures: Int = 0,
    @SerializedName("percentage")
    val percentage: Double = 0.0,
    @SerializedName("status")
    val status: String = "Eligible",
    @SerializedName("sessions")
    val sessions: List<AttendanceSessionLog> = emptyList()
) : Parcelable

@Parcelize
data class AttendanceStudentRoster(
    @SerializedName("student_id")
    val studentId: String = "",
    @SerializedName("student_name")
    val studentName: String = "",
    @SerializedName("attended")
    val attended: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("percentage")
    val percentage: Double = 100.0,
    @SerializedName("status")
    val status: String = "Eligible"
) : Parcelable

data class AttendanceRecordItem(
    @SerializedName("student_id")
    val studentId: String,
    @SerializedName("student_name")
    val studentName: String,
    @SerializedName("status")
    val status: String
)

data class RecordAttendanceRequest(
    @SerializedName("date")
    val date: String,
    @SerializedName("slot")
    val slot: String,
    @SerializedName("records")
    val records: List<AttendanceRecordItem>
)

data class AttendanceSessionResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("subject_code")
    val subjectCode: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("slot")
    val slot: String = ""
)
