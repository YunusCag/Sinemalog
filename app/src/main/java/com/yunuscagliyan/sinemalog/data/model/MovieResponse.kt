package com.yunuscagliyan.sinemalog.data.model


import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val singleMovies: List<SingleMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)