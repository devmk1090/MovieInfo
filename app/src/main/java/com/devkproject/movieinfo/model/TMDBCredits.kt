package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class TMDBCredits(
    val id: Int,
    val cast: ArrayList<TMDBCast>,
    val crew: ArrayList<TMDBCrew>
)

data class TMDBCast(
    val character: String,
    val gender: Int,
    val name: String,
    @SerializedName("profile_path")
    val picture: String,
    val credit_id: String
)

data class TMDBCrew(
    val name: String,
    val job: String,
    @SerializedName("profile_path")
    val picture: String,
    val credit_id: String
)