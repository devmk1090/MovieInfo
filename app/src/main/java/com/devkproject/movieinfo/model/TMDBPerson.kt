package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class TMDBPerson(
    val cast: ArrayList<TMDBPersonCast>
)

data class TMDBPersonCast(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("vote_average")
    val rating: Double
)