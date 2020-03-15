package com.yunuscagliyan.sinemalog.data.model


import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("credit_id")
    val creditİd: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String
)