package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class TMDBCredits(
    val id: Int,
    val cast: ArrayList<TMDBCast>
)

data class TMDBCast(
    val character: String,
    val gender: Int,
    val name: String,
    @SerializedName("profile_path")
    val picture: String,
    val order: Int
)