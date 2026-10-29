package com.example.reater.models

import com.google.gson.annotations.SerializedName


data class StudyMaterial(
    @SerializedName("filename")
    var filename: String = "",

    @SerializedName("timestamp")
    var timestamp: String = "",

    @SerializedName("authorName")
    var authorName: String = "",

    @SerializedName("authorDP")
    var authorDP: String = "",

    @SerializedName("fileLocation")
    var fileLocation: String = "",

) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "","")
}
