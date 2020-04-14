package com.devkproject.movieinfo.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val FIRST_PAGE = 1
const val PER_PAGE = 20
const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "4140785408ae0d33bd7a2220a28fa0e2"
const val POSTER_URL = "https://image.tmdb.org/t/p/w342"

//object class : 싱글턴 클래스, 여러번 호출해도 객체는 한번만 생성
object TMDBClient {
    fun getClient(): TMDBInterface {

        val requestInterceptor = Interceptor {chain ->
            val url: HttpUrl = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("language", "ko")
                .build()
            val request: Request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            //명시적으로 값을 반환
            return@Interceptor chain.proceed(request)
        }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBInterface::class.java)
    }
}