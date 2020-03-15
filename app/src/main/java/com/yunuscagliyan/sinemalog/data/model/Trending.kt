package com.yunuscagliyan.sinemalog.data.model


import com.google.gson.annotations.SerializedName

data class Trending(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val trends: List<Trend>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)