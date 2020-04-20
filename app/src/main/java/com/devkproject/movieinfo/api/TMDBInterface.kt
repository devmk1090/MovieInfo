package com.devkproject.movieinfo.api

import com.devkproject.movieinfo.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
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

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") id: Int): Call<TMDBCredits>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int,
                        @Query("region") region: String): Single<TMDBResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovie(@Query("page") page: Int,
                         @Query("region") region: String): Single<TMDBResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovie(@Query("page") page: Int,
                         @Query("region") region: String): Single<TMDBResponse>

    @GET("search/movie")
    fun getSearchMovie(@Query("query") query: String,
                       @Query("page") page: Int,
                       @Query("include_adult") adult: Boolean,
                       @Query("region") region: String): Single<TMDBResponse>

    @GET("genre/movie/list")
    fun getGenreMovie(): Call<TMDBGenre>

    @GET("discover/movie")
    fun getGenrePopularMovie(@Query("region") region: String,
                             @Query("sort_by") sortBy: String,
                             @Query("include_adult") adult: Boolean,
                             @Query("page") page: Int,
                             @Query("with_genres") genreId: String,
                             @Query("vote_count.gte") vote_count: Int): Single<TMDBResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page: Int,
                           @Query("region") region: String): Single<TMDBResponse>
}