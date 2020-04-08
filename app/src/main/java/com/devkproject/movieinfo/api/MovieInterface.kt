package com.devkproject.movieinfo.api

import com.devkproject.movieinfo.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.themoviedb.org/3/movie/popular?api_key=4140785408ae0d33bd7a2220a28fa0e2&page=1
//https://api.themoviedb.org/3/movie/530915?api_key=4140785408ae0d33bd7a2220a28fa0e2
//https://api.themoviedb.org/3
//https://api.themoviedb.org/3/search/movie?api_key=4140785408ae0d33bd7a2220a28fa0e2&query=

interface MovieInterface {

    @GET("movie/popular")
    fun listOfMovies(@Query("api_key") api_key: String,
                     @Query("page") page: Int): Call<MovieResponse>
}