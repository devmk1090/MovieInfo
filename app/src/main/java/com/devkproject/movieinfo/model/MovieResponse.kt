package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class MovieResponse (
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("results")
    val movieList: ArrayList<MovieDetail>
)
