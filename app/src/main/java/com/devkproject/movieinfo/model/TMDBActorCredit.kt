package com.devkproject.movieinfo.model

import com.google.gson.annotations.SerializedName

data class TMDBActorCredit(
    val id: String,
    val known_for: ArrayList<TMDBActorKnownFor>
)

data class TMDBActorKnownFor(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    @SerializedName("vote_average")
    val rating: Double
)