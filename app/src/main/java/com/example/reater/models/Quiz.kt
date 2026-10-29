package com.example.reater.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Quiz(
    @SerializedName("due_date")
    var quizSchedule: String? = null,

    @SerializedName("quiz_description")
    var syllabus: String? = null,

    @SerializedName("time_limit_minutes")
    var quizDuration: String? = null,

    @SerializedName("subject_id")
    var subjectId: String? = null,

    @SerializedName("questions")
    var quizList: @RawValue List<Question>? = null,

    @SerializedName("quiz_name")
    var quizname: String? = null,

    @SerializedName("id")
    var classId: String? = null

): Parcelable
