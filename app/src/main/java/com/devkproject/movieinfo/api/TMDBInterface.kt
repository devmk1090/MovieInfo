package com.devkproject.movieinfo.api

import com.devkproject.movieinfo.model.TMDBDetail
import com.devkproject.movieinfo.model.TMDBResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//https://api.themoviedb.org/3
//https://api.themoviedb.org/3/movie/popular?api_key=4140785408ae0d33bd7a2220a28fa0e2&page=1
//https://api.themoviedb.org/3/movie/530915?api_key=4140785408ae0d33bd7a2220a28fa0e2
//https://api.themoviedb.org/3/search/movie?api_key=4140785408ae0d33bd7a2220a28fa0e2&query=

interface TMDBInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<TMDBDetail>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int,
                        @Query("region") region: String): Single<TMDBResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("page") page: Int,
                         @Query("region") region: String): Single<TMDBResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovie(
                         @Query("page") page: Int,
                         @Query("region") region: String): Single<TMDBResponse>

    @GET("search/movie")
    fun getSearchMovie(@Query("query") query: String,
                       @Query("page") page: Int,
                       @Query("region") region: String): Single<TMDBResponse>
}