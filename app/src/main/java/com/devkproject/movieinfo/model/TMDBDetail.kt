package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class TMDBDetail(
    val id: Int,
    val budget: Int,
    val revenue: Int,
    val popularity: Double,
    @SerializedName("vote_average")
    val rating: Double,
    val vote_count: Int,
    val runtime: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val status: String,
    val tagline: String,
    val video: Boolean,
    val original_title: String
)