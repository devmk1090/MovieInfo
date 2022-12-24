package com.devkproject.movieinfo.model

data class TMDBVideos(
    val id: Int,
    val results: ArrayList<TMDBTrailers>
)

data class TMDBTrailers(
    val id: String,
    val key: String,
    val name: String,
    val type: String
)