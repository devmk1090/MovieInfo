package com.devkproject.movieinfo.model

data class TMDBPersonDetail(
    val name: String,
    val birthday: String,
    val deathday: String,
    val place_of_birth: String,
    val also_known_as: ArrayList<String>
)