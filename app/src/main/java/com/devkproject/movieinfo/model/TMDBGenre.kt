package com.devkproject.movieinfo.model

data class TMDBGenre(
    val genres: ArrayList<Genres>
)

data class Genres(
    val id: Int,
    val name: String
)