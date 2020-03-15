package com.yunuscagliyan.sinemalog.data.model


import com.google.gson.annotations.SerializedName

data class TVSeries(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val series: List<Series>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)