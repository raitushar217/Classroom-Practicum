package com.example.reater.models

import com.google.gson.annotations.SerializedName

data class Notice(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("section")
    val section: List<String> = emptyList(),

    @SerializedName("teacher")
    val teacher: String = "",

    @SerializedName("teacher_name")
    val teacherName: String = "",

    @SerializedName("author")
    val author: String = ""
)

data class CreateNoticeRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("section")
    val section: List<String> = listOf("CS-A")
)
