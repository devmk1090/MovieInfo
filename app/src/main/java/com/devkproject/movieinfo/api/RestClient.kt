package com.devkproject.movieinfo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.themoviedb.org/3/"


//object class : 싱글턴 클래스, 여러번 호출해도 객체는 한번만 생성
object TMDBClient {
    fun getMovieClient(): MovieInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieInterface::class.java)
    }
}